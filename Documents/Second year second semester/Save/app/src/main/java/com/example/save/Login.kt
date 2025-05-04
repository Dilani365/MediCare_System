package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use WindowInsetsControllerCompat instead of enableEdgeToEdge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.userprofile)

        // Reference the views
        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)

        // Set click listener for login button
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // For now, just navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for sign up button
        signUpButton.setOnClickListener {
            // Add navigation to sign up screen if you have one
            Toast.makeText(this, "Sign up clicked", Toast.LENGTH_SHORT).show()
        }
    }
}