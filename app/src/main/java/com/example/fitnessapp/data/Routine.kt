package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar



@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val routineId: Int = 0,
    val name: String,
    val date: Long = System.currentTimeMillis(),
    val userId: String
)

