package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashbord)

        // Reference the button
        val btn3 = findViewById<ImageView>(R.id.imageView24)

        // Set click listener to navigate to SecondActivity
        btn3.setOnClickListener {
            val intent = Intent(this, Catagory::class.java)
            startActivity(intent) // Start the new activity
        }



        // Reference the button
        val btn4 = findViewById<ImageView>(R.id.imageView23)

        // Set click listener to navigate to SecondActivity
        btn4.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent) // Start the new activity
        }
    }
}