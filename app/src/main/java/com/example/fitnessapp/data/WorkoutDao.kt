package com.example.fitnessapp.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkout(workout:Workout)

    @Query("SELECT * FROM workouts ORDER BY id ASC")
    fun getAllWorkouts(): LiveData<List<Workout>>
    @Query("DELETE FROM workouts")
    suspend fun deleteAll()


}