package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.VaccineEntity

data class Vaccine(
    val id: Int,
    val name: String,
    val description: String
)

fun VaccineEntity.toDomain() = Vaccine(id, name, description)