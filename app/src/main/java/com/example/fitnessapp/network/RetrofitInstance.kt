package com.example.fitnessapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("rapidapi-key", "fa5bf1c985mshcf82b95f2f66e15p18bbd6jsn8a9d85c4e2ab")
                .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")

            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://exercisedb.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }


}