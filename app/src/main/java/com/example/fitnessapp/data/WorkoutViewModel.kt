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

    private val _currentSets = MutableLiveData<List<WorkoutSet>>()
    val currentSets : LiveData<List<WorkoutSet>> get() = _currentSets

    private var elapsedTimeInSeconds = -1L

    private fun createDefaultSet(workoutName: String): WorkoutSet {
        return WorkoutSet(workoutName, 1, 0, 0, false)
    }
    fun updateCurrentSets(set: WorkoutSet){
        val currentSets = _currentSets.value ?: emptyList()
        val updatedSets = currentSets.toMutableList().also{
            it.add(set)
        }
        Log.d("ViewModel", "Added set ${set.workoutName}, new size: ${updatedSets.size}")

        _currentSets.postValue(updatedSets)
    }
   // To add a set
    fun updateCurrentSets(sets: List<WorkoutSet>) {
        _currentSets.postValue(sets)
    }

    // Function to remove a set
    fun removeSet(set: WorkoutSet) {
        val currentSets = _currentSets.value ?: return
        val updatedSets = currentSets.toMutableList().also {
            it.remove(set)
        }
        _currentSets.postValue(updatedSets)
    }

    fun updateCurrentWorkout(exercise: GymExercise){
        val currentExercises = _currentWorkouts.value ?: emptyList()
        val updatedExercises = currentExercises.toMutableList().also {
            it.add(exercise)
        }

//        Log.d("ViewModel", "Added exercise ${exercise.name}, new size: ${updatedExercises.size}")
//        for (n in 0 until updatedExercises.size){
//            if(updatedExercises[n].setCount == 0){
//                updatedExercises[n].setCount = 1
//            }
//        }
        val defaultSet = createDefaultSet(exercise.name)
        updateCurrentSets(defaultSet)
        _currentWorkouts.postValue(updatedExercises)
    }



    fun addSetCount(exercise: GymExercise){
        exercise.setCount++
    }

    fun removeSetCount(exercise: GymExercise){
        exercise.setCount--
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
//        viewModelScope.launch {
//            workoutDao.deleteAll()
//        }
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