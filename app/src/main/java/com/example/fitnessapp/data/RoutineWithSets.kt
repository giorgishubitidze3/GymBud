package com.example.fitnessapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithSets(
    @Embedded val routine: Routine,

    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val workoutSets : List<WorkoutSet>
)
    {
        val routineId: Int
        get() = routine.routineId
    }

