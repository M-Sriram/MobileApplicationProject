package com.example.group1_mobileapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class CandidateDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)

        // Get data from intent extras
        val imageRes = intent.getStringExtra("imageRes")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")

        // Set data to views
        Glide.with(this).load(imageRes).into(imageView)
        textViewName.text = name
        textViewDescription.text = description
    }
}
