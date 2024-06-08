package com.example.fitnessapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "template_sets",
    foreignKeys = [ForeignKey(
        entity = Template::class,
        parentColumns = ["templateId"],
        childColumns = ["templateId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TemplateSet(
    @PrimaryKey(autoGenerate = true) val newTempId: Int,
    val setId: Int,
    var templateId: Int = 0, // Foreign key to associate with a Template
    val exerciseName: String,
    val userId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TemplateSet) return false

        if (exerciseName != other.exerciseName) return false
        if (setId != other.setId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = exerciseName.hashCode()
        result = 31 * result + setId
        return result
    }
}

