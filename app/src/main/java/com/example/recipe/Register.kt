package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe.databinding.ActivityRegisterBinding
import com.example.recipe.databinding.ActivityRegisterBinding.inflate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for textRegoster
        binding.textRegister.setOnClickListener{
            navigateToLogin()
        }

        binding.icBack.setOnClickListener{
            navigateToLogin()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val email = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                firebaseAuth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            Toast.makeText(this, "Verification email sent. Please verify your email address.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                navigateToLogin()
                            } else {
                                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty field is not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LogIn::class.java)

        startActivity(intent)
        finish()
    }
}