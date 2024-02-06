package com.example.fitnessapp.network

import android.content.Context
import androidx.core.content.ContentProviderCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://exercisedb.p.rapidapi.com"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key","b5b7058d99msh0e249f9f1a4c87bp1ba7d8jsn72df5fafc8d6")
                .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
                chain.proceed(request.build())
        }
        .build()



    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }


}