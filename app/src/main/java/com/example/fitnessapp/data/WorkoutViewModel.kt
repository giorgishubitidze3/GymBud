package com.example.fitnessapp.data

import android.app.Application
import android.os.CountDownTimer
import android.system.Os.remove
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.fitnessapp.data.RoutineWithSets
import com.example.fitnessapp.data.Routine
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth


class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    val auth = Firebase.auth
    private val templateDao: TemplateDao = AppDatabase.getDatabase(application).templateDao()
    private val templateSetDao : TemplateSetDao = AppDatabase.getDatabase(application).templateSetDao()


    private val setIdCounters = mutableMapOf<String, Int>()
    private val availableSetIds = mutableMapOf<String, MutableList<Int>>()
    val allWorkoutSets: LiveData<List<RoutineWithSets>>

//    val readAllData: LiveData<List<Workout>>

    private val repository: AppRepository

    private val _workoutState: MutableLiveData<Boolean> = MutableLiveData(false)
    val workoutState: LiveData<Boolean> = _workoutState

    private val _templateState: MutableLiveData<Boolean> = MutableLiveData(false)
    val templateState: LiveData<Boolean> get() = _templateState

    private val _currentWorkouts = MutableLiveData<List<GymExercise>>()
        .apply { value = emptyList() }
    val currentWorkouts: LiveData<List<GymExercise>> get() = _currentWorkouts

    private var timer: CountDownTimer? = null

    private val _elapsedTime = MutableLiveData<String>()
    val elapsedtime: LiveData<String> get() = _elapsedTime

    private val _currentSets = MutableLiveData<List<WorkoutSet>>()
    val currentSets: LiveData<List<WorkoutSet>> get() = _currentSets

    private val _currentRoutineName = MutableLiveData<String>("Workout")
    val currentRoutineName: LiveData<String> get() = _currentRoutineName

    private val _currentTemplatesWithSets = MediatorLiveData<List<TemplateWithSets>>()
    val currentTemplatesWithSets: LiveData<List<TemplateWithSets>> get() = _currentTemplatesWithSets


    private var elapsedTimeInSeconds = -1L

    private val _currentUserUsername = MutableLiveData<String>()
    val currentUserUsername : LiveData<String> get() = _currentUserUsername

    private val _currentUserFullName = MutableLiveData<String>()
    val currentUserFullName : LiveData<String> get() = _currentUserFullName

    val currentUserId = auth.currentUser?.uid.toString()


    private fun createDefaultSet(workoutName: String): WorkoutSet {
        return WorkoutSet(0,generateUniqueSetId(workoutName), 0, workoutName,0, 0, 0,false, currentUserId )

//        return WorkoutSet(generateUniqueSetId(workoutName), 0, workoutName,0, 0, 0,false)
    }


    fun updateCurrentUserData(username:String,name:String,surname:String) {
        _currentUserUsername.postValue(username)
        var fullName = "${name} ${surname}"
        _currentUserFullName.postValue(fullName)
    }

    fun getAllCompletedSets(): List<WorkoutSet> {
        val list: List<WorkoutSet> = _currentSets.value ?: emptyList()

        val latestRoutineIdLiveData = getLastRoutine()
        var latestRoutineId = 0

        latestRoutineIdLiveData.observeForever { routineWithSets ->
            latestRoutineId = routineWithSets?.routineId ?: 0
        }

        val completedSets = list.filter { it.isCompleted }

        return completedSets.map { it.copy(routineId = latestRoutineId + 1) }
    }


    fun getRoutineObj(): Routine{
        return Routine(0,_currentRoutineName.value.toString(),System.currentTimeMillis(), currentUserId)
    }


    fun getTemplateObj(): Template{
        return Template(0,_currentRoutineName.value.toString(),currentUserId)
    }


    fun resetCurrentRoutineName(){
        _currentRoutineName.value = "Workout"
    }

    fun generateUniqueSetId(workoutName: String): Int {
        val currentSets = _currentSets.value ?: return 1

        // Filter out removed sets for the given workoutName
        val remainingSets = currentSets.filter { it.workoutName == workoutName }
        val existingIds = remainingSets.map { it.setId }.toSet()

        // Find the next available ID that is not used
        var nextId = 1
        while (existingIds.contains(nextId)) {
            nextId++
        }

        Log.d("Function", "generateUniqueSetId: $nextId for $workoutName")
        return nextId

    }

    fun decreaseSetId(workoutName: String, setId: Int) {
        availableSetIds.computeIfAbsent(workoutName) { mutableListOf() }.add(setId)
        Log.d("Function", "decreaseSetId: $setId for $workoutName")
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
        Log.d("Function", "addSet: ${newSet.setId} for ${newSet.workoutName}")
    }



    fun changeRoutineName(name: String){
        _currentRoutineName.value = name
    }


    fun resetCurrentSets() {
        val updatedSets = emptyList<WorkoutSet>()
        _currentSets.postValue(updatedSets)
        Log.d("Function", "resetCurrentSets")
    }

    fun resetCurrentWorkouts() {
        val updatedWorkouts = emptyList<GymExercise>()
        _currentWorkouts.postValue(updatedWorkouts)
        Log.d("Function", "resetCurrentWorkouts")
    }

    fun updateWorkoutSetValues(workoutName: String, currentKg: Int, currentReps: Int, isCompleted: Boolean) {
        val currentSets = _currentSets.value ?: return
        val updatedSets = currentSets.map { set ->
            if (set.workoutName == workoutName) {
                set.copy(
                    currentKg = currentKg,
                    currentReps = currentReps,
                    isCompleted = isCompleted
                )
            } else {
                set
            }
        }
        _currentSets.postValue(updatedSets)
    }

    fun workoutSetToTemplateSet(workoutSet: WorkoutSet, templateId: Int): TemplateSet {
        return TemplateSet(
            newTempId = 0,
            setId = workoutSet.setId,
            templateId = templateId,
            exerciseName = workoutSet.workoutName,
            userId = auth.currentUser?.uid.toString()
        )
    }

    fun removeSet(set: WorkoutSet) {
        val currentSets = _currentSets.value ?: return
        if (currentSets.size <= 1) {
            Log.d("WorkoutViewModel", "Cannot remove the last set for ${set.workoutName}")
            return
        }
        val updatedSets = currentSets.toMutableList().apply { remove(set) }
        _currentSets.postValue(updatedSets)
        decreaseSetId(set.workoutName, set.setId)
        Log.d("Function", "removeSet: ${set.setId} for ${set.workoutName}")
    }

    fun updateCurrentWorkout(exercise: GymExercise) {
        val currentExercises = _currentWorkouts.value ?: emptyList()
        val updatedExercises = currentExercises.toMutableList().also {
            it.add(exercise)
        }
        val defaultSet = createDefaultSet(exercise.name)
        addSet(defaultSet)
        _currentWorkouts.postValue(updatedExercises)
        Log.d("Function", "updateCurrentWorkout: ${exercise.name}")
    }


    fun startTemplateMaker(){
        _templateState.value = true
    }

    fun endTemplateMaker(){
        _templateState.value = false
    }


    fun startWorkout() {
        _workoutState.value = true
    }

    fun endWorkout() {
        if (_workoutState.value != false) {
            _workoutState.value = false
        } else {
            Log.d("WorkoutState", "The workoutState is already false")
        }
    }

    private fun combineTemplateAndSets(templates: List<Template>?, templateSets: List<TemplateSet>?) {
        if (templates != null && templateSets != null) {
            val templateWithSetsList = templates.map { template ->
                val setsForTemplate = templateSets.filter { it.templateId == template.templateId }
                TemplateWithSets(template, setsForTemplate)
            }
            _currentTemplatesWithSets.value = templateWithSetsList
        }
    }

    init {
        Log.d("WorkoutState", "ViewModel created")
//        val workoutDao = AppDatabase.getDatabase(application).workoutDao()
//        repository = AppRepository(workoutDao)
//        readAllData = repository.readAllData
//        initializeTimer()

        val templatesLiveData = templateDao.getAllTemplates(auth.currentUser?.uid.toString())
        val templateSetsLiveData = templateSetDao.getAllTemplateSets(auth.currentUser?.uid.toString())

        _currentTemplatesWithSets.addSource(templatesLiveData){templates ->
            combineTemplateAndSets(templates, templateSetsLiveData.value)
        }

        _currentTemplatesWithSets.addSource(templateSetsLiveData) { templateSets ->
            combineTemplateAndSets(templatesLiveData.value, templateSets)
        }

        val routineDao = AppDatabase.getDatabase(application).routineDao()
        val templateDao = AppDatabase.getDatabase(application).templateDao()
        repository = AppRepository(routineDao, templateDao, templateSetDao)
        allWorkoutSets = repository.getAllRoutinesWithSets()
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

    fun insertRoutineWithSets(routine: Routine, sets: List<WorkoutSet>) {
        viewModelScope.launch {
            repository.insertRoutineWithSets(routine, sets)
        }
    }

    fun updateTemplateName(templateId : Int, name:String){
        viewModelScope.launch{
            repository.updateTemplateName(templateId,name)
        }
    }

    fun deleteTemplateById(templateId: Int){
        viewModelScope.launch{
            repository.deleteTemplateById(templateId)
            repository.deleteTemplateSetById(templateId)
        }
    }


    fun insertTemplateWithSets(template: Template, sets: List<WorkoutSet>){

        val listOfTemplateSets : MutableList<TemplateSet> = mutableListOf()

        sets.forEach { set -> listOfTemplateSets.add(workoutSetToTemplateSet(set, 0)) }

        viewModelScope.launch{
            repository.insertTemplateWithSets(template,listOfTemplateSets)
        }
    }

    fun getAllRoutinesWithSets(): LiveData<List<RoutineWithSets>> {
        return repository.getAllRoutinesWithSets()
    }

    fun getRoutineWithSets(routineId: Int): LiveData<RoutineWithSets> {
        return repository.getRoutineWithSets(routineId)
    }

    fun getLastRoutine(): LiveData<RoutineWithSets>{
        return repository.getLastRoutine()
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }


    fun startTimer() {
        timer?.start()
    }

    fun stopTimer() {
        timer?.cancel()
        elapsedTimeInSeconds = -1L
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WorkoutState", "ViewModel cleared")
    }

//    fun addWorkout(workout: Workout) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.addWorkout(workout)
//        }
//    }

    fun updateCurrentSet(updatedSet: WorkoutSet) {
        val currentSets = _currentSets.value?.toMutableList() ?: mutableListOf()
        val setIndex = currentSets.indexOfFirst { it.setId == updatedSet.setId && it.workoutName == updatedSet.workoutName }
        if (setIndex != -1) {
            currentSets[setIndex] = updatedSet
            _currentSets.postValue(currentSets)
            Log.d("Function", "updateCurrentSet: ${updatedSet.setId} for ${updatedSet.workoutName}")
        } else {
            Log.d("Function", "updateCurrentSet: Set not found")
        }
    }
}




