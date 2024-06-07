package com.example.fitnessapp

import android.app.Application
import androidx.room.Room
import com.example.fitnessapp.data.AppDatabase
import com.google.firebase.FirebaseApp

class MyApplication: Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database").build()
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}