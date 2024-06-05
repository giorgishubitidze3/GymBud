package com.example.fitnessapp.data

import androidx.lifecycle.LiveData

class AppRepository(private val routineDao: RoutineDao) {

    suspend fun insertRoutineWithSets(routine: Routine, sets: List<WorkoutSet>) {
        val routineId = routineDao.insertRoutine(routine)
        sets.forEach { it.routineId = routineId.toInt() }
        routineDao.insertWorkoutSets(sets)
    }

    fun getAllRoutinesWithSets(): LiveData<List<RoutineWithSets>> {
        return routineDao.getAllRoutinesWithSets()
    }

    fun getRoutineWithSets(routineId: Int): LiveData<RoutineWithSets> {
        return routineDao.getRoutineWithSets(routineId)
    }

    fun getLastRoutine(): LiveData<RoutineWithSets>{
        return routineDao.getLastRoutine()
    }

    suspend fun clearAllData() {
        routineDao.clearAllWorkoutSets()
        routineDao.clearAllRoutines()
    }






//    val readAllData: LiveData<List<Workout>> = workoutDao.getAllWorkouts()
//
//    suspend fun addWorkout(workout: Workout){
//        workoutDao.insertWorkout(workout)
//    }
}

