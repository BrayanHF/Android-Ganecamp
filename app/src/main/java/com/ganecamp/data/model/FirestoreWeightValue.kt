package com.ganecamp.data.model

import com.ganecamp.domain.model.WeightValue
import com.google.firebase.Timestamp

data class FirestoreWeightValue(
    val value: Float = 0f, val date: Timestamp = Timestamp.now()
)

fun FirestoreWeightValue.toWeightValue(): WeightValue {
    return WeightValue(
        value = value, date = date.toInstant()
    )
}

fun WeightValue.toFirestoreWeightValue(): FirestoreWeightValue {
    return FirestoreWeightValue(
        value = value, date = Timestamp(date)
    )
}