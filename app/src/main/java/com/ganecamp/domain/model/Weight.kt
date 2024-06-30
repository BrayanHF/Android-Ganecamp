package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.WeightEntity
import java.time.ZonedDateTime

data class Weight (
    val id: Int,
    val animalId: Int,
    val weight: Float,
    val date: ZonedDateTime
)

fun WeightEntity.toDomain() = Weight(id, animalId, weight, date)