package com.example.kernel.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kernel.Components.MyAccount
import com.example.kernel.R
import com.example.kernel.UI.MainActivity
import com.example.kernel.UI.OrganizerActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var etName : EditText
    private lateinit var etPassword : TextInputEditText
    private lateinit var etEmail : EditText
    private lateinit var etPhone : TextInputEditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnRegister: Button
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etPhone = view.findViewById(R.id.etPhone)
        radioGroup = view.findViewById(R.id.radioGroup)
        btnRegister = view.findViewById(R.id.btnRegister)

        etPassword.setText("")
        etPassword.setSelection(0)

        etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                etPassword.postDelayed({
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(etPassword, InputMethodManager.SHOW_IMPLICIT)
                }, 100)
            }
        }

        etPassword.setOnClickListener {
            etPassword.isFocusableInTouchMode = true
            etPassword.isFocusable = true
            etPassword.requestFocus()
        }

        etPhone.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                etPhone.postDelayed({
                    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(etPhone, InputMethodManager.SHOW_IMPLICIT)
                }, 100)
            }
        }

        radioGroup.setOnCheckedChangeListener { _: RadioGroup?, _: Int ->
            btnRegister.isEnabled =
                true
        }

        btnRegister.setOnClickListener {
            val name = etName.text?.toString()
            val email = etEmail.text?.toString()
            val password = etPassword.text?.toString()
            val phone = etPhone.text?.toString()
            firebaseAuth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance().getReference("Accounts")

            val selectedId = radioGroup.checkedRadioButtonId
            val selectedRadioButton: RadioButton? = view.findViewById(selectedId)

            if(selectedRadioButton!=null) {
                val userType = selectedRadioButton.text.toString()

                if (name?.isNotEmpty() == true &&
                    email?.isNotEmpty() == true &&
                    password?.isNotEmpty() == true &&
                    phone?.isNotEmpty() == true) {
                    try {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    val account = MyAccount(name, email, phone, userType)
                                    Toast.makeText(this.context, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

                                    val uid = it.result.user?.uid.toString()
                                    database.child(uid).setValue(account)
                                    if (userType == "Organizer"){
                                        val intent = Intent(this.context, OrganizerActivity::class.java)
                                        intent.putExtra("email", email)
                                        intent.putExtra("uid", uid)
                                        intent.putExtra("userType", userType)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        requireActivity().finish()
                                    } else {
                                        val intent = Intent(this.context, MainActivity::class.java)
                                        intent.putExtra("email", email)
                                        intent.putExtra("uid", uid)
                                        intent.putExtra("userType", userType)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }
                                } else {
                                    exceptionHandler(it.exception)
                                }
                            }
                    } catch (e : Error){
                        Toast.makeText(this.context,
                            "There is a problem from our side,\nPlease try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (name?.isEmpty() == true) {
                        etName.error = "Name is required"
                    }
                    if (email?.isEmpty() == true) {
                        etEmail.error = "Email is required"
                    }
                    if (!email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }!!) {
                        etEmail.error = "Enter a valid email address"
                    }
                    if (password != null) {
                        if (password.isEmpty()) {
                            etPassword.error = "Password is required"
                        }
                    }
                    if (password != null) {
                        if (password.length < 8) {
                            etPassword.error = "Password must be at least 8 characters"
                        } else {
                            etPassword.error = null
                        }
                    }
                }
            }
        }
    }

    private fun exceptionHandler(exception: Exception?) {
        when (exception) {
            is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(this.context, "Weak password: ${exception.reason}", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(this.context, "Invalid email format", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this.context, "Email already exists!\nElse User does not exist.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this.context, "Error: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}