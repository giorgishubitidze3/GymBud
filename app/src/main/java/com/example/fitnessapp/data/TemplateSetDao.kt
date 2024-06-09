package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TemplateSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplateSet(templateSet : TemplateSet)


    @Query("SELECT * FROM template_sets WHERE userId = :userId")
    fun getAllTemplateSets(userId : String): LiveData<List<TemplateSet>>

    @Query("DELETE FROM template_sets")
    suspend fun clearAllTemplateSets()

    @Query("DELETE FROM template_sets WHERE templateId = :templateId")
    suspend fun deleteTemplateSetById(templateId:Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTemplateSets(sets: List<TemplateSet>)

}