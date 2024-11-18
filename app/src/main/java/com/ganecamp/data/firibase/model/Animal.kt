package com.ganecamp.data.firibase.model

import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Animal(
    @get:Exclude @set:Exclude var id: String? = null,
    val tag: String = "",
    val nickname: String? = null,
    val lotId: String? = null,
    val animalGender: AnimalGender = AnimalGender.Male,
    val animalBreed: AnimalBreed = AnimalBreed.ZEBU,
    val birthDate: Timestamp = Timestamp.now(),
    val purchaseValue: Double = 0.0,
    val purchaseDate: Timestamp = Timestamp.now(),
    val saleValue: Double = 0.0,
    val saleDate: Timestamp = Timestamp.now(),
    val animalState: AnimalState = AnimalState.Healthy
)