package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Reference the button inside onCreate
        val btn3 = findViewById<ImageView>(R.id.imageView26)

        // Set click listener inside onCreate
        btn3.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent) // Start the new activity
        }
    }
}
