package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true) val templateId: Int = 0,
    val name: String,
    val userId: String
)
