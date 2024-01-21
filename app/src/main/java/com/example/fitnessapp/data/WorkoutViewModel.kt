package com.example.fitnessapp.data

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WorkoutViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Workout>>
    private val repository: AppRepository

    private val _workoutState: MutableLiveData<Boolean> = MutableLiveData(false)
    val workoutState : LiveData<Boolean> = _workoutState

    private val _currentWorkouts = MutableLiveData<List<GymExercise>>()
        .apply { value = emptyList() }
    val currentWorkouts: LiveData<List<GymExercise>> get() = _currentWorkouts

    private var timer: CountDownTimer? = null

    private val _elapsedTime = MutableLiveData<String>()
    val elapsedtime: LiveData<String> get() = _elapsedTime

    private var elapsedTimeInSeconds = -1L

    fun updateCurrentWorkout(exercise: GymExercise){
        val currentExercises = _currentWorkouts.value ?: emptyList()
        val updatedExercises = currentExercises.toMutableList().also {
            it.add(exercise)
        }

        Log.d("ViewModel", "Added exercise ${exercise.name}, new size: ${updatedExercises.size}")

        _currentWorkouts.postValue(updatedExercises)
    }

    fun startWorkout(){
        _workoutState.value = true
    }

    fun endWorkout(){
        if (_workoutState.value != false) {
            _workoutState.value = false
        } else {
            Log.d("WorkoutState", "The workoutState is already false")
        }
    }

    init{
        Log.d("WorkoutState", "ViewModel created")
        val workoutDao = AppDatabase.getDatabase(application).workoutDao()
        repository = AppRepository(workoutDao)
        readAllData = repository.readAllData
        initializeTimer()
    }

    private fun initializeTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                elapsedTimeInSeconds++
                val minutes = elapsedTimeInSeconds / 60
                val seconds = elapsedTimeInSeconds % 60
                val formattedTime = String.format("%02d:%02d", minutes, seconds)
                _elapsedTime.postValue(formattedTime)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }
        }
    }
    fun startTimer() {
        // Start the timer
        timer?.start()
    }

    fun stopTimer() {
        // Stop the timer
        timer?.cancel()
        elapsedTimeInSeconds = -1L
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WorkoutState", "ViewModel cleared")
        // ... cleanup code
    }

    fun addWorkout(workout: Workout){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWorkout(workout)
        }
    }
}