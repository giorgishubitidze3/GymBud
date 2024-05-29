package com.example.fitnessapp.data

data class WorkoutSet(
    val workoutName: String,
    val setId: Int,
    val prevSet: Int,
    var currentKg: Int,
    var currentReps: Int,
    val isCompleted: Boolean
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
