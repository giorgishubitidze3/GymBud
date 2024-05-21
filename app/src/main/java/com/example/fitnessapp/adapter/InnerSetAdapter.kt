package com.example.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.WorkoutSet


class InnerSetAdapter(val list: List<WorkoutSet>): RecyclerView.Adapter<InnerSetAdapter.InnerViewHolder>() {

    inner class InnerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val prevSet = itemView.findViewById<TextView>(R.id.previousSetTV)
        val editTextKg = itemView.findViewById<EditText>(R.id.etKG)
        val editTextRep = itemView.findViewById<EditText>(R.id.etREP)
        val completedCheckBox = itemView.findViewById<CheckBox>(R.id.setCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inner_rv_item, parent , false)
        return InnerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val currentSet = list[position]
        holder.editTextKg.hint = currentSet.currentKg.toString()
        holder.editTextRep.hint = currentSet.currentReps.toString()
    }
}