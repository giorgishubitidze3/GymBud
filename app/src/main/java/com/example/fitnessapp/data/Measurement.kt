package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class Measurement(
   @PrimaryKey(autoGenerate = true) val measurementId: Int = 0,
    val name: String,
    val measurement: Double,
    val date: Long = System.currentTimeMillis(),
    val userId: String
)
