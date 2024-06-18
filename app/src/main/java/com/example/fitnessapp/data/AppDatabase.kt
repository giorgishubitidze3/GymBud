package com.example.fitnessapp.data

import android.content.Context
import androidx.room.*

@Database(entities = [WorkoutSet::class, Routine::class, Template::class , TemplateSet::class, Measurement::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun routineDao(): RoutineDao // Add RoutineDao
    abstract fun templateDao(): TemplateDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun templateSetDao() : TemplateSetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
