package com.example.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.Challenge
import com.google.android.material.imageview.ShapeableImageView


class ChallengesAdapter(): RecyclerView.Adapter<ChallengesAdapter.ViewHolder>() {

    var list: List<Challenge> = listOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ShapeableImageView>(R.id.image_view_challenge)
        val title = itemView.findViewById<TextView>(R.id.tv_challenge_name)
        val goal = itemView.findViewById<TextView>(R.id.tv_challenge_goal)
        val percentage = itemView.findViewById<TextView>(R.id.tv_challenge_percentage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.challenges_rv_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
    }


    fun setData(list: List<Challenge>){
        this.list = list
        notifyDataSetChanged()
    }
}