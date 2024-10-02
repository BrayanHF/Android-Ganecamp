package com.ganecamp.model.objects

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Lot(
    @get:Exclude @set:Exclude var id: String? = null,
    val name: String = "",
    val numberAnimals: Int = 0,
    val purchaseDate: Timestamp = Timestamp.now(),
    val purchaseValue: Double = 0.0,
    val saleDate: Timestamp = Timestamp.now(),
    val saleValue: Double = 0.0,
    val sold: Boolean = false
)