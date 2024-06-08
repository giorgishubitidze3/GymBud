package com.example.fitnessapp.data

import androidx.lifecycle.LiveData

class AppRepository(private val routineDao: RoutineDao, private val templateDao: TemplateDao) {

    suspend fun insertRoutineWithSets(routine: Routine, sets: List<WorkoutSet>) {
        val routineId = routineDao.insertRoutine(routine)
        sets.forEach { it.routineId = routineId.toInt() }
        routineDao.insertWorkoutSets(sets)
    }

    suspend fun insertTemplateWithSets(template: Template, sets:List<TemplateSet>){
        val templateId = templateDao.insertTemplate(template)
        sets.forEach{it.templateId = templateId.toInt()}
        templateDao.insertTemplateSets(sets)
    }

    fun getAllRoutinesWithSets(): LiveData<List<RoutineWithSets>> {
        return routineDao.getAllRoutinesWithSets()
    }//TODO checkout this doesnt return sets maybe

    fun getAllTemplatesWithSets(): LiveData<List<TemplateWithSets>>{
        return templateDao.getAllTemplatesWithSets()
    }  //TODO checkout this doesnt return sets maybe

    fun getRoutineWithSets(routineId: Int): LiveData<RoutineWithSets> {
        return routineDao.getRoutineWithSets(routineId)
    }

    fun getLastRoutine(): LiveData<RoutineWithSets>{
        return routineDao.getLastRoutine()
    }

    fun getLastTemplate(): LiveData<TemplateWithSets>{
        return templateDao.getLastTemplate()
    }

    suspend fun clearAllData() {
        routineDao.clearAllWorkoutSets()
        routineDao.clearAllRoutines()
        templateDao.clearAllTemplates()
        templateDao.clearAllTemplates()
    }



}

