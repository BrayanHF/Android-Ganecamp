package com.ganecamp.data.model

import com.ganecamp.domain.model.Weight
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreWeight(
    @get:Exclude @set:Exclude var id: String? = null,
    val weight: Float = 0f,
    val date: Timestamp = Timestamp.now()
)

fun FirestoreWeight.toWeight(): Weight {
    return Weight(
        id = id, weight = weight, date = date.toInstant()
    )
}

fun Weight.toFirestoreWeight(): FirestoreWeight {
    return FirestoreWeight(
        id = id, weight = weight, date = Timestamp(date)
    )
}