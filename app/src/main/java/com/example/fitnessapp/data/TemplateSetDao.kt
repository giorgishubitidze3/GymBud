package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface TemplateSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateSet(templateSet : TemplateSet)

    @Query("INSERT INTO template_sets (newTempId, setId, templateId,exerciseName,userId) VALUES (:newTempId, :setId, :templateId, :exerciseName, :userId)")
    suspend fun insertTemplateSetById(newTempId: Int, setId: Int, templateId: Int, exerciseName:String, userId:String)

    @Transaction
    suspend fun deleteAndReplaceTemplateSets(templateId: Int, newSets: List<TemplateSet>) {
        deleteTemplateSetById(templateId)
        // Make sure all sets have the correct templateId before inserting
        newSets.forEach { it.templateId = templateId }
        newSets.forEach { set ->
            insertTemplateSet(set)
        }
    }

    @Query("SELECT * FROM template_sets WHERE userId = :userId")
    fun getAllTemplateSets(userId : String): LiveData<List<TemplateSet>>

    @Query("DELETE FROM template_sets")
    suspend fun clearAllTemplateSets()

    @Query("DELETE FROM template_sets WHERE templateId = :templateId")
    suspend fun deleteTemplateSetById(templateId:Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTemplateSets(sets: List<TemplateSet>)

}