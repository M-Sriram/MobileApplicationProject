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
        textView.text = "Welcome to the Intro Activity"

        // Initialize ImageView and TextView to display candidate information
        val imageViewCandidate = findViewById<ImageView>(R.id.imageViewCandidate)
        val textViewCandidateName = findViewById<TextView>(R.id.textViewCandidateName)

        // Load candidate's image from intent extras using Glide
        val candidateImageRes = intent.getStringExtra("candidateImageRes")
        Glide.with(this).load(candidateImageRes).into(imageViewCandidate)

        // Logout the user when the app starts
        FirebaseAuth.getInstance().signOut()

        val btnGoToCandidate = findViewById<Button>(R.id.btnGoToCandidate)
        btnGoToCandidate.setOnClickListener {
            // Check if the user is logged in
            if (FirebaseAuth.getInstance().currentUser != null) {
                // Start the CandidateActivity
                val intent = Intent(this, CandidateActivity::class.java)
                startActivity(intent)
            } else {
                // Show a toast message
                Toast.makeText(this@IntroActivity, "You are not logged in, Please login", Toast.LENGTH_SHORT).show()
                // Start the LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // Set up click listener for the Login button
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            // Start the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set up click listener for the Logout button
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Logout the user
            FirebaseAuth.getInstance().signOut()
            // Show a toast message
            Toast.makeText(this@IntroActivity, "You have been logged out", Toast.LENGTH_SHORT).show()
        }

        // Initialize headerTextView text only if candidateName is not null
        val candidateName = intent.getStringExtra("candidateName")
        if (candidateName != null) {
            val textViewCandidateName = findViewById<TextView>(R.id.textViewCandidateName)
            textViewCandidateName.text = "\n $candidateName" // Set candidate's name in TextView with appropriate spacing
        }
    }
}
