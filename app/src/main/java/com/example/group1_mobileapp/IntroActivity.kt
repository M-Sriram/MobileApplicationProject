package com.example.group1_mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val textView = findViewById<TextView>(R.id.textView)

        val imageViewCandidate = findViewById<ImageView>(R.id.imageViewCandidate)
        val textViewCandidateName = findViewById<TextView>(R.id.textViewCandidateName)

        val candidateImageRes = intent.getStringExtra("candidateImageRes")
        Glide.with(this).load(candidateImageRes).into(imageViewCandidate)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val candidateName = intent.getStringExtra("candidateName")
            textViewCandidateName.text = "\n $candidateName"

            val btnGoToCandidate = findViewById<Button>(R.id.btnGoToCandidate)
            btnGoToCandidate.setOnClickListener {
                val intent = Intent(this, CandidateActivity::class.java)
                startActivity(intent)
            }

            val btnGoToHome = findViewById<Button>(R.id.btnGoToHome)
            btnGoToHome.setOnClickListener {
                val intent = Intent(this, PostActivity::class.java)
                startActivity(intent)
            }
        } else {

            Toast.makeText(this@IntroActivity, "You are not logged in, Please login", Toast.LENGTH_SHORT).show()

            val btnLogin = findViewById<Button>(R.id.btnLogin)
            btnLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            imageViewCandidate.setImageResource(0)
            textViewCandidateName.text=""
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this@IntroActivity, "You have been logged out", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
