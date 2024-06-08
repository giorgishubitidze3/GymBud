package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: Template): Long

    @Transaction
    @Query("SELECT * FROM templates WHERE userId = :userId")
    fun getAllTemplates(userId : String): LiveData<List<Template>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateSets(sets : List<TemplateSet>)

    @Transaction
    @Query("SELECT * FROM templates")
    fun getAllTemplatesWithSets(): LiveData<List<TemplateWithSets>>

    @Query("SELECT * FROM routines WHERE name = :name")
    suspend fun getRoutineByName(name: String): List<Routine>
    @Query("DELETE FROM routines")
    suspend fun clearAllTemplates()

    @Query("DELETE FROM template_sets")
    suspend fun clearAllTemplateSets()

    @Transaction
    @Query("SELECT * FROM templates WHERE templateId = (SELECT MAX(templateId) FROM templates)")
    fun getLastTemplate(): LiveData<TemplateWithSets>

}