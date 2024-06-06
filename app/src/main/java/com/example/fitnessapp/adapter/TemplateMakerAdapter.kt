package com.example.fitnessapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.fragment.TemplateMakerFragment

class TemplateMakerAdapter(val context: Context): RecyclerView.Adapter<TemplateMakerAdapter.ViewHolder>() {

    var list: List<GymExercise> = listOf()
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val muscleGroup = itemView.findViewById<TextView>(R.id.tvClass2Template)
        val workoutName = itemView.findViewById<TextView>(R.id.tvTitle2Template)
        val image = itemView.findViewById<ImageView>(R.id.imageViewMuscle2Template)
        val greenOverlay = itemView.findViewById<View>(R.id.overlayTemplate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_rv_item, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWorkout = list[position]

        holder.muscleGroup.text = currentWorkout.bodyPart
        holder.workoutName.text = currentWorkout.name

        val gifUrl = currentWorkout.gifUrl
        val resourceId = context.resources.getIdentifier(gifUrl, "drawable", context.packageName)
        holder.image.setImageResource(resourceId)


    }

    fun setData(list:List<GymExercise>){
        this.list = list
        notifyDataSetChanged()
    }

}