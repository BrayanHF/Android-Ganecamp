package com.ganecamp.domain.model

import java.time.Instant

data class Lot(
    val id: String? = null,
    val name: String = "",
    val purchasedAnimals: Int = 0,
    val purchaseDate: Instant = Instant.now(),
    val purchaseValue: Double = 0.0,
    val saleDate: Instant = Instant.now(),
    val saleValue: Double = 0.0,
    val sold: Boolean = false,
    val animalCount: Int = 0
)