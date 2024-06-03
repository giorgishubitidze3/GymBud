package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSet(workoutSet: WorkoutSet)

    @Query("SELECT * FROM workout_sets")
    fun getAllWorkoutSets(): LiveData<List<WorkoutSet>>

    @Query("DELETE FROM workout_sets")
    suspend fun clearAllWorkoutSets()
}