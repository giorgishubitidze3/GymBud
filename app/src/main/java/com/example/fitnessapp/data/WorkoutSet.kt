package com.example.fitnessapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="workout_sets",
    foreignKeys = [ForeignKey(
        entity = Routine::class,
        parentColumns = ["routineId"],
        childColumns = ["routineId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val newId: Int,
    val setId: Int,
    var routineId: Int  = 0,// Foreign key to associate with a Routine
    val workoutName: String,
    val prevSet: Int,
    var currentKg: Int,
    var currentReps: Int,
    var isCompleted: Boolean,
    val userId: String
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
