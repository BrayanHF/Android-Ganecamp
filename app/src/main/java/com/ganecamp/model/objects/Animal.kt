package com.ganecamp.model.objects

import com.ganecamp.utilities.enums.Gender
import com.ganecamp.utilities.enums.State
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Animal(
    @get:Exclude @set:Exclude var id: String? = null,
    val tag: String = "",
    val lotId: String? = null,
    val gender: Gender = Gender.Male,
    val breed: String = "",
    val birthDate: Timestamp = Timestamp.now(),
    val purchaseValue: Double = 0.0,
    val purchaseDate: Timestamp = Timestamp.now(),
    val saleValue: Double = 0.0,
    val saleDate: Timestamp = Timestamp.now(),
    val state: State = State.Healthy,
)