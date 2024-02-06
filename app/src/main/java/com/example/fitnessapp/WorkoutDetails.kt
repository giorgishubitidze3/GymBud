package com.example.fitnessapp

import android.content.Context
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
            val gifUrl = selectedExercise.gifUrl
            val resourceId = requireContext().resources?.getIdentifier(gifUrl,"drawable", requireContext().packageName)

            if (resourceId != null) {
                val imageView = view?.findViewById<ImageView>(R.id.detailImage)

                if (imageView != null) {
                    Glide.with(requireContext())
                        .load(resourceId)
                        .into(imageView)
                }
            }




        }else {
            Log.e("WorkoutDetails", "Selected exercise is null.")
        }
    }
}