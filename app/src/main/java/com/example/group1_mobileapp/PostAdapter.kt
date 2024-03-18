package com.example.group1_mobileapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.Date

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTextView: TextView = itemView.findViewById(R.id.postTextView);
        val userNameTextView = itemView.findViewById<TextView>(R.id.userNameTextView);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        val postText = currentItem.text
        val timestamp = currentItem.timestamp
        val userName = currentItem.userName
        val formattedTime = DateFormat.getDateTimeInstance().format(Date(timestamp)) // Format timestamp
        holder.postTextView.text = "$postText\n\nPosted on: $formattedTime" // Display post text and timestamp

        holder.userNameTextView.text = currentItem.userName
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}