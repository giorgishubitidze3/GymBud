package com.example.fitnessapp.data

data class WorkoutSet(
    val workoutName: String,
    val prevSet: Int,
    var currentKg: Int,
    var currentReps: Int,
    var isCompleted: Boolean
)
