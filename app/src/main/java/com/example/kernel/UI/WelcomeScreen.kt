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

class WelcomeScreen : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
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
        setContentView(R.layout.activity_welcome_screen)

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

        firebaseAuth = FirebaseAuth.getInstance()
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
}