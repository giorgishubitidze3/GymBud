package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RoutineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSets(sets: List<WorkoutSet>)

    @Transaction
    @Query("SELECT * FROM routines")
    fun getAllRoutinesWithSets(): LiveData<List<RoutineWithSets>>

    @Transaction
    @Query("SELECT * FROM routines WHERE date BETWEEN :startOfWeek AND :endOfWeek")
    suspend fun getRoutinesForCurrentWeek(startOfWeek: Long, endOfWeek: Long): List<RoutineWithSets>


    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineWithSets(routineId: Int): LiveData<RoutineWithSets>

    @Query("DELETE FROM workout_sets")
    suspend fun clearAllWorkoutSets()

    @Query("DELETE FROM routines")
    suspend fun clearAllRoutines()

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = (SELECT MAX(routineId) FROM routines)")
    fun getLastRoutine(): LiveData<RoutineWithSets>
}