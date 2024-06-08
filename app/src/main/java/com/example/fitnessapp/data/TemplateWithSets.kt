package com.example.fitnessapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class TemplateWithSets(
    @Embedded val template: Template,

    @Relation(
        parentColumn = "templateId",
        entityColumn = "templateId"
    )
    val templateSets: List<TemplateSet>
) {
    val templateId: Int
        get() = template.templateId
}