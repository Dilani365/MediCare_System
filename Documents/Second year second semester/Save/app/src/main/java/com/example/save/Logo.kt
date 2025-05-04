package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Logo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logo)


        val logo = findViewById<ImageView>(R.id.imageView29)

        // Set click listener to navigate to SecondActivity
        logo.setOnClickListener {

            val intent = Intent(this, Login::class.java)
            startActivity(intent) // Start the new activity
        }

    }
}