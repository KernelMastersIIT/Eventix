package com.example.kernel.UI

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kernel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WelcomeScreen : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var textSwitcher : TextSwitcher
    private lateinit var btnLogin: Button
    private lateinit var tvCreate: TextView
    private val descriptions = listOf(
        "We're excited to help you book and manage your service appointments with ease.",
        "Get the latest updates and news instantly.",
        "Enjoy a seamless experience with our new features."
    )
    private var index = 0
    private val handler = Handler(Looper.getMainLooper())

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Accounts")

        if (firebaseAuth.currentUser != null) {
            val userMail = firebaseAuth.currentUser!!.email
            database.get().addOnSuccessListener { snack ->
                for(uid in snack.children){
                    if(uid.child("email").value.toString() == userMail){
                        initializeLogin(userMail, uid.key.toString(), uid.child("userType").value.toString())
                        return@addOnSuccessListener
                    }
                }
            }
        } else {
            setContentView(R.layout.activity_welcome_screen)
            setupUI()
        }
    }

    private fun setupUI() {
        textSwitcher = findViewById(R.id.textSwitcher)
        btnLogin = findViewById(R.id.btnLogin)
        tvCreate = findViewById(R.id.tvCreate)

        textSwitcher.setFactory {
            TextView(this).apply {
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.darker_gray, theme))
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
        }

        textSwitcher.inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        textSwitcher.outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)

        startTextSwitching()

        btnLogin.setOnClickListener {
            intent = Intent(this, SignInUp::class.java)
            startActivity(intent)
        }

        tvCreate.setOnClickListener {
            intent = Intent(this, SignInUp::class.java)
            startActivity(intent)
        }
    }

    private fun startTextSwitching() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                textSwitcher.setText(descriptions[index])
                index = (index + 1) % descriptions.size
                handler.postDelayed(this, 3000)
            }
        }, 0)
    }

    private fun initializeLogin(userMail: String, uid : String, userType : String) {
        if (userType == "Organizer"){
            val intent = Intent(this, OrganizerActivity::class.java)
            intent.putExtra("email", userMail)
            intent.putExtra("uid", uid)
            intent.putExtra("userType", userType)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", userMail)
            intent.putExtra("uid", uid)
            intent.putExtra("userType", userType)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}