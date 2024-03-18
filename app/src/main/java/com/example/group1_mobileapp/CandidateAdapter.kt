package com.example.group1_mobileapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class CandidateAdapter(options: FirebaseRecyclerOptions<Candidate>) : FirebaseRecyclerAdapter<Candidate, CandidateAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int,
        model: Candidate
    ) {
        Glide.with(holder.imgPhoto.context).load(model.image).into(holder.imgPhoto)
        val candi: Candidate = model
        holder.txtName.text = candi.name
        holder.cardItem.setOnClickListener {
            var intent : Intent = Intent(holder.cardItem.context,CandidateDetailsActivity::class.java)
            intent.putExtra("imageRes", candi.image)
            intent.putExtra("name", candi.name)
            intent.putExtra("description", candi.description)
            holder.cardItem.context.startActivity(intent)
        }

    }
    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_candidate, parent, false))
    {
        val txtName: TextView = itemView.findViewById(R.id.textView)
        val imgPhoto: ImageView = itemView.findViewById(R.id.imageView)
        val cardItem :LinearLayout = itemView.findViewById(R.id.linearRow)
    }

}