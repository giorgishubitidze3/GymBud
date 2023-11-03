package com.example.fitnessapp.data


import com.google.gson.annotations.SerializedName

data class GymExercise(
    @SerializedName("bodyPart")
    val bodyPart: String,
    @SerializedName("equipment")
    val equipment: String,
    @SerializedName("gifUrl")
    val gifUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instructions")
    val instructions: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>,
    @SerializedName("target")
    val target: String
)