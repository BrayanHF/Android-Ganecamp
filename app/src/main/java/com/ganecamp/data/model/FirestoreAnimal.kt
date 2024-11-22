package com.ganecamp.data.model

import com.ganecamp.domain.enums.AnimalBreed
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.domain.enums.AnimalState
import com.ganecamp.domain.model.Animal
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreAnimal(
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

fun FirestoreAnimal.toAnimal(): Animal {
    return Animal(
        id = id,
        tag = tag,
        nickname = nickname,
        lotId = lotId,
        animalGender = animalGender,
        animalBreed = animalBreed,
        birthDate = birthDate.toInstant(),
        purchaseValue = purchaseValue,
        purchaseDate = purchaseDate.toInstant(),
        saleValue = saleValue,
        saleDate = saleDate.toInstant(),
        animalState = animalState
    )
}

fun Animal.toFirestoreAnimal(): FirestoreAnimal {
    return FirestoreAnimal(
        id = id,
        tag = tag,
        nickname = nickname,
        lotId = lotId,
        animalGender = animalGender,
        animalBreed = animalBreed,
        birthDate = Timestamp(birthDate.epochSecond, birthDate.nano),
        purchaseValue = purchaseValue,
        purchaseDate = Timestamp(purchaseDate.epochSecond, purchaseDate.nano),
        saleValue = saleValue,
        saleDate = Timestamp(saleDate.epochSecond, saleDate.nano),
        animalState = animalState
    )
}