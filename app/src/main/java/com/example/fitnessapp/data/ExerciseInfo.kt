package com.example.fitnessapp.data

data class ExerciseInfo(
    val setCount: Int,
    val exerciseName: String,
    val weight: List<Double>,
    val reps: List<Int>
)
