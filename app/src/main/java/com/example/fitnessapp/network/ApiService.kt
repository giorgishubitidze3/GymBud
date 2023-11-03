package com.example.fitnessapp.network


import com.example.fitnessapp.data.GymExercise
import retrofit2.http.GET

interface ApiService {

    @GET("/exercises")
    suspend fun getExercise(): List<GymExercise>
}