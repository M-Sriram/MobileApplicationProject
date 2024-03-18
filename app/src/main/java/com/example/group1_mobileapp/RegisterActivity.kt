package com.example.group1_mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private var authentic: FirebaseAuth? = null
    private lateinit var btnRegis: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authentic = FirebaseAuth.getInstance()

        btnRegis = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)

        btnRegis.setOnClickListener {
            inputValidation()
        }
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun inputValidation() {
        val email = findViewById<EditText>(R.id.loginEmail)?.text.toString().trim()
        val password = findViewById<EditText>(R.id.loginPassword)?.text.toString().trim()
        val confirmPassword = findViewById<EditText>(R.id.loginCPassword)?.text.toString().trim()
        val name = findViewById<EditText>(R.id.loginName)?.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show()
            return
        }

        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Confirm Password required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        doRegister(name, email, password)
    }

    private fun doRegister(name: String, email: String, password: String) {
        authentic?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = authentic?.currentUser
                    val userId = user?.uid ?: ""

                    // Update user profile with name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // User profile updated successfully
                                // Now store user details in Firestore
                                val userData = hashMapOf(
                                    "name" to name,
                                    "email" to email,
                                    "uid" to userId
                                )

                                val db = FirebaseFirestore.getInstance()
                                db.collection("users")
                                    .document(userId)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        // User details saved successfully in Firestore
                                        Log.d("RegisterActivity", "User details stored successfully in Firestore")

                                        // Also store user details in Realtime Database
                                        val dbRealtime = FirebaseDatabase.getInstance().reference
                                        val userRealtimeData = hashMapOf(
                                            "uid" to userId,
                                            "name" to name,
                                            "email" to email
                                            // Add other user data here if needed
                                        )
                                        dbRealtime.child("users")
                                            .child(userId)
                                            .setValue(userRealtimeData)
                                            .addOnCompleteListener { realtimeTask ->
                                                if (realtimeTask.isSuccessful) {
                                                    // User details saved successfully in Realtime Database
                                                    Log.d("RegisterActivity", "User details stored successfully in Realtime Database")
                                                    startActivity(Intent(this, HomeActivity::class.java))
                                                } else {
                                                    // Failed to save user details in Realtime Database
                                                    Log.e("RegisterActivity", "Failed to store user details in Realtime Database")
                                                    Toast.makeText(this, "Failed to register: ${realtimeTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        // Failed to save user details in Firestore
                                        Log.e("RegisterActivity", "Failed to store user details in Firestore: ${e.message}")
                                        Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Failed to update user profile
                                Log.e("RegisterActivity", "Failed to update profile")
                                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Registration failed
                    Log.e("RegisterActivity", "Registration failed: ${task.exception?.message}")
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }?.addOnFailureListener {
                // Firebase authentication failed
                Log.e("RegisterActivity", "Firebase authentication failed: ${it.message}")
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}
