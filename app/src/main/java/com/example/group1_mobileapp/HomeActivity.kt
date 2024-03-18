package com.example.group1_mobileapp

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.group1_mobileapp.Post

class HomeActivity : AppCompatActivity() {

    private lateinit var editTextPost: EditText
    private lateinit var btnSubmitPost: Button
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: ArrayList<Post>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        editTextPost = findViewById(R.id.editTextPost)
        btnSubmitPost = findViewById(R.id.btnSubmitPost)
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts)

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("posts")
        auth = FirebaseAuth.getInstance()

        // Initialize post list
        postList = ArrayList()

        // Initialize adapter
        postAdapter = PostAdapter(postList)

        // Set RecyclerView layout manager
        recyclerViewPosts.layoutManager = LinearLayoutManager(this)

        // Set adapter to RecyclerView
        recyclerViewPosts.adapter = postAdapter

        // Set onClickListener for posting
        btnSubmitPost.setOnClickListener {
            val newPostText = editTextPost.text.toString().trim()
            if (newPostText.isNotEmpty()) {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val userId = user.uid
                    val userName = user.displayName ?: ""
                    val newPost = Post(userId, userName, newPostText, System.currentTimeMillis())
                    savePostToFirebase(newPost)
                    saveUserDataToDatabase(userId, userName) // Save user data to users collection
                    editTextPost.text.clear()
                }
            }
        }

        // Load initial posts
        loadInitialPosts()
    }

    private fun savePostToFirebase(post: Post) {
        val postRef = databaseReference.push()
        postRef.setValue(post)
    }

    private fun loadInitialPosts() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (snapshot in dataSnapshot.children) {
                    val userId = snapshot.child("userId").value as? String
                    val userName = snapshot.child("userName").value as? String
                    val text = snapshot.child("text").value as? String
                    val timestamp = snapshot.child("timestamp").value as? Long
                    if (userId != null && userName != null && text != null && timestamp != null) {
                        val post = Post(userId, userName, text, timestamp)
                        postList.add(post)
                    }
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HomeActivity", "Error loading initial posts", databaseError.toException())
            }
        })
    }

    private fun saveUserDataToDatabase(userId: String, userName: String) {
        val db = FirebaseDatabase.getInstance().reference
        val user = HashMap<String, Any>()
        user["uid"] = userId

        db.child("users").child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("HomeActivity", "User data saved successfully")
                } else {
                    Log.e("HomeActivity", "Failed to save user data", task.exception)
                }
            }
    }
}