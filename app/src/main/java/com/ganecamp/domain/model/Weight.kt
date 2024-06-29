package com.ganecamp.domain.model

import java.time.ZonedDateTime

data class Weight (
    val id: Int,
    val animalId: Int,
    val weight: Double,
    val date: ZonedDateTime
)