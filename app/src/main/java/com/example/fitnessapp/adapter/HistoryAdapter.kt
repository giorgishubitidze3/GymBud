package com.example.fitnessapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.Workout


class HistoryAdapter(): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var workoutList = emptyList<Workout>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv1 = itemView.findViewById<TextView>(R.id.tv1)
//        val tv2 = itemView.findViewById<TextView>(R.id.tv2)
        val tv3 = itemView.findViewById<TextView>(R.id.tv3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workoutList[position]

        holder.tv1.text = workout.name
//        holder.tv2.text = workout.bodyPart
        holder.tv3.text = workout.gifUrl
    }


    fun setData(workout: List<Workout>){
        this.workoutList = workout
        notifyDataSetChanged()
    }

}