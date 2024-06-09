package com.example.fitnessapp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.SharedViewModel

class WorkoutViewModelFactory(
    private val application: Application,
    private val sharedViewModel: SharedViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(application, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}