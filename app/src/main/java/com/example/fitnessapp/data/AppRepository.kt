package com.example.fitnessapp.data

import android.util.Log
import androidx.lifecycle.LiveData

class AppRepository(private val routineDao: RoutineDao, private val templateDao: TemplateDao, private val templateSetDao: TemplateSetDao, private val workoutSetDao: WorkoutSetDao, private val measurementDao: MeasurementDao) {

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


    suspend fun getAllRoutines(currentUserId : String): List<Routine>{
        return routineDao.getAllRoutines(currentUserId)
    }

    suspend fun getWorkoutSetsForRoutineIds(routineIds: List<Int>): List<WorkoutSet> {
        return workoutSetDao.getWorkoutSetsForCurrentWeek(routineIds)
    }

    suspend fun getAllMeasurementsByName(name: String, userId:String): List<Measurement>{
        return measurementDao.getAllMeasurementsByName(name,userId)
    }

    suspend fun insertMeasurement(measurement: Measurement){
        Log.d("MeasurementDetail","app repository function called insertMeasurement")
        return measurementDao.insertMeasurement(measurement)
    }

    suspend fun updateMeasurementByName(id: Int, userId: String, measurementValue: Double){
        return measurementDao.updateMeasurementById(id,userId,measurementValue)
    }
    suspend fun deleteMeasurementById(id:Int, userId: String){
        return measurementDao.deleteMeasurementById(id,userId)
    }

    suspend fun getRoutinesForPeriod(startDate: Long, endDate: Long, currentUserId: String): List<Routine> {
        return routineDao.getRoutinesForPeriod(startDate, endDate,currentUserId)
    }

    suspend fun updateTemplateName(templateId: Int , name: String){
        templateDao.updateTemplateName(templateId,name)
    }

    suspend fun updateTemplateSets(templateId: Int, newSets: List<TemplateSet>) {

        templateSetDao.deleteAndReplaceTemplateSets(templateId, newSets)
    }

        suspend fun deleteTemplateById(templateId: Int) {
            templateDao.deleteTemplateById(templateId)
        }


        suspend fun deleteTemplateSetById(templateId: Int) {
            templateSetDao.deleteTemplateSetById(templateId)
        }

        fun getAllTemplatesWithSets(): LiveData<List<TemplateWithSets>> {
            return templateDao.getAllTemplatesWithSets()
        }

        fun getAllTemplates(userId: String): LiveData<List<Template>> {

            val templateList = templateDao.getAllTemplates(userId)
            Log.d("templateSet", "This is from app repository ${templateList.value.toString()}")
            return templateList
        }

        fun getRoutineWithSets(routineId: Int): LiveData<RoutineWithSets> {
            return routineDao.getRoutineWithSets(routineId)
        }

        fun getLastRoutine(): LiveData<RoutineWithSets> {
            return routineDao.getLastRoutine()
        }

        fun getLastTemplate(): LiveData<TemplateWithSets> {
            return templateDao.getLastTemplate()
        }

        suspend fun clearAllData() {
            routineDao.clearAllWorkoutSets()
            routineDao.clearAllRoutines()
            templateDao.clearAllTemplates()
            templateDao.clearAllTemplates()
        }



}

