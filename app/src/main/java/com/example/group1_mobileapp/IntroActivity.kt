package com.example.group1_mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "Welcome to the Intro Activity"

        val btnGoToCandidate = findViewById<Button>(R.id.btnGoToCandidate)
        btnGoToCandidate.setOnClickListener {
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)
        }

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
