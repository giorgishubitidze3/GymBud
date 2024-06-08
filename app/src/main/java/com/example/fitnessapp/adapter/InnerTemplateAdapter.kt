package com.example.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.TemplateSet

class InnerTemplateAdapter(val list: List<TemplateSet>): RecyclerView.Adapter<InnerTemplateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val innerText = itemView.findViewById<TextView>(R.id.template_inner_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_rv_inner_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.distinctBy { it.exerciseName }.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentExerciseName = list.distinctBy { it.exerciseName }[position].exerciseName
        val count = list.count { it.exerciseName == currentExerciseName }

        holder.innerText?.text = "$count x $currentExerciseName"
    }

}