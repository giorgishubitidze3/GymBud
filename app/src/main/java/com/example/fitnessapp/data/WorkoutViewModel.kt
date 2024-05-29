package com.example.fitnessapp.data

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application): AndroidViewModel(application) {

    private val setIdCounters = mutableMapOf<String, Int>()

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
        return WorkoutSet(workoutName, increaseSetId(workoutName), 1, 0, 0, false)
    }

    fun increaseSetId(workoutName: String): Int {
        val currentCounter = setIdCounters[workoutName] ?: 0
        val newCounter = currentCounter + 1
        setIdCounters[workoutName] = newCounter
        return newCounter
    }

    fun decreaseSetId(workoutName: String) {
        val currentCounter = setIdCounters[workoutName] ?: return
        if (currentCounter > 1) {
            setIdCounters[workoutName] = currentCounter - 1
        } else {
            setIdCounters[workoutName] = 0
        }
    }


    fun addSet(newSet: WorkoutSet) {
        val currentSets = _currentSets.value?.toMutableList() ?: mutableListOf()

        val existingSetIndex = currentSets.indexOfFirst { it.workoutName == newSet.workoutName && it.setId == newSet.setId }
        if (existingSetIndex != -1) {
            currentSets[existingSetIndex] = newSet
        } else {
            currentSets.add(newSet)
        }

        _currentSets.postValue(currentSets)
    }




//    fun updateCurrentSets(newSet: WorkoutSet) {
//        val currentSets = _currentSets.value?.toMutableList() ?: mutableListOf()
//
//        if (currentSets.any { it.workoutName == newSet.workoutName && it.setId == newSet.setId }) {
//            val existingSet = currentSets.find { it.workoutName == newSet.workoutName && it.setId == newSet.setId }
//            if (existingSet != null) {
//                val updatedSet = existingSet.copy(
//                    currentKg = newSet.currentKg,
//                    currentReps = newSet.currentReps,
//                    isCompleted = newSet.isCompleted
//                )
//                currentSets.remove(existingSet)
//                currentSets.add(updatedSet)
//            }
//        } else {
//            currentSets.add(newSet)
//        }
//
//        _currentSets.postValue(currentSets)
//    }


    fun resetCurrentSets(){
        val updatedSets = emptyList<WorkoutSet>()
        _currentSets.postValue(updatedSets)
    }
    fun resetCurrentWorkouts(){
        val updatedWorkouts = emptyList<GymExercise>()
        _currentWorkouts.postValue(updatedWorkouts)
    }
    fun updateWorkoutSetValues(workoutName: String, currentKg: Int, currentReps: Int, isCompleted:Boolean){
        val currentSets = _currentSets.value ?: return
        val updatedSets = currentSets.map {set ->
            if(set.workoutName == workoutName){
                set.copy(
                    currentKg= currentKg,
                    currentReps = currentReps,
                    isCompleted = isCompleted
                )
            }else{
                set
            }
        }

        _currentSets.postValue(updatedSets)

    }

    fun updateCurrentSet(updatedSet: WorkoutSet) {
        val currentSets = _currentSets.value ?: emptyList()
        val updatedSets = currentSets.map { if (it.workoutName == updatedSet.workoutName) updatedSet else it }
        _currentSets.postValue(updatedSets)
    }




   // To add a set
    fun addSet(sets: List<WorkoutSet>) {
        _currentSets.postValue(sets)
    }

    // Function to remove a set
//    fun removeSet(set: WorkoutSet) {
//        val currentSets = _currentSets.value ?: return
//        val updatedSets = currentSets.toMutableList().also {
//            it.remove(set)
//        }
//        _currentSets.postValue(updatedSets)
//    }



    fun removeSet(set: WorkoutSet) {
        val currentSets = _currentSets.value ?: return
        if (currentSets.size <= 1) {
            Log.d("WorkoutViewModel", "Cannot remove the last set for ${set.workoutName}")
            return
        }
        val updatedSets = currentSets.toMutableList().apply { remove(set) }
        _currentSets.postValue(updatedSets)
        decreaseSetId(set.workoutName)
    }

//    fun removeSet(set: WorkoutSet) {
//        val currentSets = _currentSets.value ?: return
//        val setsForExercise = currentSets.filter { it.workoutName == set.workoutName }
//        if (setsForExercise.size > 1) {
//            val updatedSets = currentSets.toMutableList().apply {
//                remove(set)
//            }
//            _currentSets.postValue(updatedSets)
//        } else {
//            Log.d("WorkoutViewModel", "Cannot remove the last set for ${set.workoutName}")
//        }
//    }

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
        addSet(defaultSet)
        _currentWorkouts.postValue(updatedExercises)
    }



//    fun addSetCount(exercise: GymExercise){
//        exercise.setCount++
//    }
//
//    fun removeSetCount(exercise: GymExercise) {
//        if (exercise.setCount > 0) exercise.setCount--
//    }

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