package com.ganecamp.domain.model

import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import java.time.Instant

data class Animal(
    var id: String? = null,
    val tag: String = "",
    val nickname: String? = null,
    val lotId: String? = null,
    val animalGender: AnimalGender = AnimalGender.Male,
    val animalBreed: AnimalBreed = AnimalBreed.ZEBU,
    val birthDate: Instant = Instant.now(),
    val purchaseValue: Double = 0.0,
    val purchaseDate: Instant = Instant.now(),
    val saleValue: Double = 0.0,
    val saleDate: Instant = Instant.now(),
    val animalState: AnimalState = AnimalState.Healthy
)