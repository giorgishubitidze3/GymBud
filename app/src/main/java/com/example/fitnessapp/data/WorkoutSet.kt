package com.example.fitnessapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="workout_sets")
data class WorkoutSet(
    @ColumnInfo(name = "workout_name") val workoutName: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "set_id") val setId: Int,
    @ColumnInfo(name = "prev_set") val prevSet: Int,
    @ColumnInfo(name = "current_kg") var currentKg: Int,
    @ColumnInfo(name = "current_reps")  var currentReps: Int,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkoutSet) return false

        if (workoutName != other.workoutName) return false
        if (setId != other.setId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workoutName.hashCode()
        result = 31 * result + setId
        return result
    }
}
