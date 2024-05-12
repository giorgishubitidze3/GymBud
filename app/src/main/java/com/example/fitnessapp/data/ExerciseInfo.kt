package com.example.fitnessapp.data

data class ExerciseInfo(
    val setCount: Int,
    val exerciseName: String,
    val weight: String,
    val reps: String,
    var sessionId: Int = 1
)
