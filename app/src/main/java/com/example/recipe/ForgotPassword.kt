package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe.databinding.ActivityForgotpasswordBinding
import com.example.recipe.databinding.ActivityForgotpasswordBinding.inflate
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotpasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for textRegister
        binding.textBackToLogin.setOnClickListener{
            navigateToLogin()
        }

        binding.icBackToLogin.setOnClickListener{
            navigateToLogin()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSubmit.setOnClickListener{
            val emailInput = binding.emailInput.text.toString()
            if (emailInput.isNotEmpty()) {
                if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(emailInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Reseting Password Has Send to Your Email", Toast.LENGTH_SHORT).show()
                                navigateToLogin()
                            } else {
                                Toast.makeText(this, "Failed to send email", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun navigateToLogin(){
        // create intent
        val intent = Intent(this, LogIn::class.java)

        startActivity(intent)
        finish()
    }
}