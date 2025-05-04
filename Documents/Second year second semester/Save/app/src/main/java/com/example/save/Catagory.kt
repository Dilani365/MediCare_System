package com.example.save

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Catagory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)


        val logo = findViewById<ImageView>(R.id.imageView9)

        // Set click listener to navigate to SecondActivity
        logo.setOnClickListener {

            val intent = Intent(this, CatagoryList::class.java)
            startActivity(intent) // Start the new activity
        }

    }
}