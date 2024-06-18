package com.example.fitnessapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.GymExercise

class MeasurementsAdapter(private val switchToDetailCallback: (String) -> Unit):RecyclerView.Adapter<MeasurementsAdapter.ViewHolder>() {

    private val list = listOf<String>(
        "Weight",
        "Body fat",
        "Neck",
        "Shoulders",
        "Chest",
        "Left bicep",
        "Right bicep",
        "Left forearm",
        "Right forearm",
        "Upper abs",
        "Lower abs",
        "Waist",
        "Hips",
        "Left thigh",
        "Right thigh",
        "Left calf",
        "Right calf"
    )

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.measurementItemTitle)
        val description = itemView.findViewById<TextView>(R.id.measurementItemDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_measurement, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.title.text = currentItem

        holder.itemView.setOnClickListener{
            switchToDetailCallback(currentItem)
        }
    }
}