package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Second : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.swcondpage)

        // Reference the button
        val btn3 = findViewById<Button>(R.id.button)

        // Set click listener to navigate to SecondActivity
        btn3.setOnClickListener {
            val intent = Intent(this, Third::class.java)
            startActivity(intent) // Start the new activity
        }
    }
}