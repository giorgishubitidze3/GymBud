package com.example.fitnessapp.network


import com.example.fitnessapp.data.GymExercise
import retrofit2.http.GET

interface ApiService {

    @GET("/exercises/bodyPart/back?limit=200")
    suspend fun getExercise(): List<GymExercise>
}