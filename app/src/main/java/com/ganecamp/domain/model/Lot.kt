package com.ganecamp.domain.model

import com.ganecamp.data.model.SimpleLotData

data class Lot(
    val id: Int,
    val animalCount: Int
)

fun SimpleLotData.toDomain() = Lot(id, animalCount)