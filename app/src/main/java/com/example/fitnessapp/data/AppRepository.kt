package com.example.fitnessapp.data

import androidx.lifecycle.LiveData

class AppRepository(private val workoutDao: WorkoutDao) {

    val readAllData: LiveData<List<Workout>> = workoutDao.getAllWorkouts()

    suspend fun addWorkout(workout: Workout){
        workoutDao.insertWorkout(workout)
    }
}

