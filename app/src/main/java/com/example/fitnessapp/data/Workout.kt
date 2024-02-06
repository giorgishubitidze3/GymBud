package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
//    val bodyPart: String,
    val  lastWeight: Int,
    val lastReps: Int,
    var bestSet: BestSet,
    val gifUrl:String,
    var setCount: Int,
    var weightsAndReps: String,
    var sessionId: Int
)


data class WeightsAndReps(
    val weights: Int,
    val reps: Int
)

data class BestSet(
    val weights:Int,
    val reps: Int
)