package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Third : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thirdpage)

        // Reference the button
        val btn3 = findViewById<Button>(R.id.button2)

        // Set click listener to navigate to SecondActivity
        btn3.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent) // Start the new activity
        }
    }
}