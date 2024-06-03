package com.example.fitnessapp.data

import androidx.lifecycle.LiveData

class AppRepository(private val workoutSetDao: WorkoutSetDao) {

    val allWorkoutSets: LiveData<List<WorkoutSet>> = workoutSetDao.getAllWorkoutSets()

    suspend fun insert(workoutSet: WorkoutSet) {
        workoutSetDao.insertWorkoutSet(workoutSet)
    }

    suspend fun clearAll() {
        workoutSetDao.clearAllWorkoutSets()
    }






//    val readAllData: LiveData<List<Workout>> = workoutDao.getAllWorkouts()
//
//    suspend fun addWorkout(workout: Workout){
//        workoutDao.insertWorkout(workout)
//    }
}

