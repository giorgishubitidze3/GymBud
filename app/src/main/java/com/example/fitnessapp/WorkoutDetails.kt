package com.example.fitnessapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.bumptech.glide.Glide
import com.example.fitnessapp.data.GymExercise


class WorkoutDetails : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedExercise = arguments?.getParcelable<GymExercise>("selectedExercise")
        Log.d("WorkoutDetails", "Selected exercise in WorkoutDetails: $selectedExercise")


        if (selectedExercise != null) {
            view?.findViewById<TextView>(R.id.tvDetailName)?.text = selectedExercise.name
            view?.findViewById<TextView>(R.id.tvDetailMuscle)?.text = selectedExercise.bodyPart
            view?.findViewById<TextView>(R.id.tvDetailDescription)?.text = selectedExercise.instructions.joinToString("\n")

            view?.findViewById<ImageView>(R.id.detailImage)
                ?.let { Glide.with(it.context).load(selectedExercise.gifUrl).into(view.findViewById<ImageView>(R.id.detailImage)) }


        }else {
            Log.e("WorkoutDetails", "Selected exercise is null.")
        }
    }
}