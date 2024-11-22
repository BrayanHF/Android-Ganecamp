package com.ganecamp.data.model

import com.ganecamp.domain.model.Lot
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreLot(
    @get:Exclude @set:Exclude var id: String? = null,
    val name: String = "",
    val purchasedAnimals: Int = 0,
    val purchaseDate: Timestamp = Timestamp.now(),
    val purchaseValue: Double = 0.0,
    val saleDate: Timestamp = Timestamp.now(),
    val saleValue: Double = 0.0,
    val sold: Boolean = false,
    val animalCount: Int = 0
)

fun FirestoreLot.toLot(): Lot {
    return Lot(
        id = id,
        name = name,
        purchasedAnimals = purchasedAnimals,
        purchaseDate = purchaseDate.toInstant(),
        purchaseValue = purchaseValue,
        saleDate = saleDate.toInstant(),
        saleValue = saleValue,
        sold = sold,
        animalCount = animalCount
    )
}

fun Lot.toFirestoreLot(): FirestoreLot {
    return FirestoreLot(
        id = id,
        name = name,
        purchasedAnimals = purchasedAnimals,
        purchaseDate = Timestamp(purchaseDate),
        purchaseValue = purchaseValue,
        saleDate = Timestamp(saleDate),
        saleValue = saleValue,
        sold = sold,
        animalCount = animalCount
    )
}