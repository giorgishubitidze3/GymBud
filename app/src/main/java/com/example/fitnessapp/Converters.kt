package com.example.fitnessapp

import androidx.room.TypeConverter
import com.example.fitnessapp.data.BestSet
import com.example.fitnessapp.data.WeightsAndReps
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMap(map: Map<Int, Int>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toMap(json: String): Map<Int, Int> {
        val type = object : TypeToken<Map<Int, Int>>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromBestSet(bestSet: BestSet): String {
        return Gson().toJson(bestSet)
    }

    @TypeConverter
    fun toBestSet(json: String): BestSet {
        val type = object : TypeToken<BestSet>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromWeightsAndRepsList(list: List<WeightsAndReps>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toWeightsAndRepsList(json: String): List<WeightsAndReps> {
        val type = object : TypeToken<List<WeightsAndReps>>() {}.type
        return Gson().fromJson(json, type)
    }
}