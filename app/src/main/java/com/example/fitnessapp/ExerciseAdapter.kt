package com.example.fitnessapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fitnessapp.data.GymExercise

class ExerciseAdapter(
    var exercises: List<GymExercise>,
    private val switchToDetailCallback: () -> Unit
): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    fun setData(newData: List<GymExercise>) {
        exercises = newData
        notifyDataSetChanged()
    }

    inner class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvClass = itemView.findViewById<TextView>(R.id.tvClass)
        val imageViewMuscle = itemView.findViewById<ImageView>(R.id.imageViewMuscle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        var exercise = exercises[position]

        holder.tvTitle.text = exercise.name
        holder.tvClass.text = exercise.bodyPart
        holder.imageViewMuscle.load(exercise.gifUrl)

        holder.itemView.setOnClickListener{
            switchToDetailCallback()
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }





}