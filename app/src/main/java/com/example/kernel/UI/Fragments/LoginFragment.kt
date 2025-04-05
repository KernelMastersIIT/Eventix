package com.example.kernel.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.example.kernel.R
import com.example.kernel.UI.MainActivity
import com.example.kernel.UI.OrganizerActivity
import com.example.kernel.UI.SignInUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val database : DatabaseReference = FirebaseDatabase.getInstance().getReference("Accounts")
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var etEmail : EditText
    private lateinit var etPassword : EditText
    private lateinit var tvForgotPassword : TextView
    private lateinit var btnLogin : Button
    private lateinit var btnGoogle : Button
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
                result ->
            if(result.resultCode == RESULT_OK){
                val accountTask : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val signInAccount : GoogleSignInAccount = accountTask.result
                    val authCredential : AuthCredential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)

                    firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
                        if(it.isSuccessful){
                            val user = firebaseAuth.currentUser
                            if(user != null){
                                firebaseAuth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                                    if (reloadTask.isSuccessful) {
                                        val userMail = firebaseAuth.currentUser?.email

                                        if (userMail != null) {
                                            var flag = 0
                                            database.get().addOnSuccessListener { snack ->
                                                for(uid in snack.children){
                                                    if(uid.child("email").value.toString() == userMail){
                                                        flag = 1
                                                        initializeLogin(userMail, uid.key.toString(), uid.child("userType").value.toString())
                                                    }
                                                }
                                            }
                                            if(flag == 0) firebaseAuth.removeAuthStateListener {
                                                Log.d("AuthDebug", "User email does not exist in Accounts!")
                                                Toast.makeText(requireContext(),
                                                    "No Account with this email!\nPlease Register.",
                                                    Toast.LENGTH_SHORT)
                                            }
                                        } else {
                                            Log.e("AuthDebug", "User email still NULL after reload")
                                        }
                                    } else {
                                        Log.e("AuthDebug", "User reload failed", reloadTask.exception)
                                    }
                                }
                            } else {
                                Log.e("AuthDebug", "Sign-in successful, but user is null")
                            }
                        } else {
                            Log.e("AuthDebug", "Firebase Sign-In failed", it.exception)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this.context,
                            "There is a problem from our side,\nPLease try again later!",
                            Toast.LENGTH_SHORT).show()
                    }
                } catch (e : Exception){
                    Toast.makeText(this.context,
                        "There is a problem from our side,\nPLease try again later!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnGoogle = view.findViewById(R.id.btnGoogle)

        tvForgotPassword.setOnClickListener {

        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8) {
                readData(email, password)
            } else {
                if (email.isEmpty()) {
                    etEmail.error = "Email is required"
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = "Enter a valid email address"
                }
                if (password.isEmpty()) {
                    etPassword.error = "Password is required"
                }
                if (password.length < 8) {
                    etPassword.error = "Password must be at least 8 characters"
                }
            }
        }

        btnGoogle.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.G_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), options)

            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    val intent = googleSignInClient.signInIntent
                    activityResultLauncher.launch(intent)
                }
            }
        }
    }

    private fun initializeLogin(userMail: String, uid : String, userType : String) {
<<<<<<< HEAD
        if (userType == "Organizer"){
            val intent = Intent(this.context, OrganizerActivity::class.java)
=======
        val ctx = context ?: return
        if (userType == "Organizer"){
            val intent = Intent(ctx, OrganizerActivity::class.java)
>>>>>>> cfdf3db (Project Files)
            intent.putExtra("email", userMail)
            intent.putExtra("uid", uid)
            intent.putExtra("userType", userType)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        } else {
<<<<<<< HEAD
            val intent = Intent(this.context, MainActivity::class.java)
=======
            val intent = Intent(ctx, MainActivity::class.java)
>>>>>>> cfdf3db (Project Files)
            intent.putExtra("email", userMail)
            intent.putExtra("uid", uid)
            intent.putExtra("userType", userType)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun readData(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    database.get().addOnSuccessListener { snack ->
                        for(uid in snack.children){
                            if(uid.child("email").value.toString() == email){
                                initializeLogin(email, uid.key.toString(), uid.child("userType").value.toString())
                            }
                        }
                    }
                } else {
                    exceptionHandler(it.exception)
                }
            }.addOnFailureListener {
                Toast.makeText(this.context,
                    "There is a problem from our side,\nPlease try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun exceptionHandler(exception: Exception?) {
        when (exception) {
            is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(this.context, "Weak password: ${exception.reason}", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(this.context, "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this.context, "This email is already registered. Try another sign-in method.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this.context, "Error: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}