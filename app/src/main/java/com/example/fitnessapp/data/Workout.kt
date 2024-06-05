package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
//    val bodyPart: String,  //TODO checkout later
    val  weight: String,
    val reps: String,
    var setCount: Int,
    var sessionId: Int
)
