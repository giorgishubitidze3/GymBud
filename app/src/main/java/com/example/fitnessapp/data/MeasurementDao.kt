package com.example.fitnessapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MeasurementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: Measurement)

    suspend fun insertMeasurement(name: String, value: Double, date: Long, userId: String) {
        val measurement = Measurement(name = name, measurement = value, date = date, userId = userId)
        insertMeasurement(measurement)
    }

    @Query("SELECT * FROM measurements WHERE userId = :userId")
    suspend fun getAllMeasurements(userId: String): List<Measurement>

    @Query("SELECT * FROM measurements WHERE name = :name AND userId = :userId")
    suspend fun getAllMeasurementsByName(name: String, userId:String): List<Measurement>

    @Query("UPDATE measurements SET measurement = :measurementValue WHERE measurementId = :id AND userId = :userId")
    suspend fun updateMeasurementById(id: Int, userId: String, measurementValue: Double)

    @Query("DELETE FROM measurements WHERE measurementId = :id and userId = :userId")
    suspend fun deleteMeasurementById(id: Int, userId: String)

    @Query("DELETE FROM measurements WHERE userId = :userId")
    suspend fun clearAllMeasurements(userId: String)

}