package com.ganecamp.data.model

import com.ganecamp.domain.model.VaccineApplied
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreVaccineApplied(
    @get:Exclude @set:Exclude var id: String? = null,
    val vaccineId: String = "",
    val date: Timestamp = Timestamp.now(),
    val name: String = "",
    val description: String = ""
)

fun FirestoreVaccineApplied.toVaccineApplied(): VaccineApplied {
    return VaccineApplied(
        id = id,
        vaccineId = vaccineId,
        date = date.toInstant(),
        name = name,
        description = description
    )
}

fun VaccineApplied.toFirestoreVaccineApplied(): FirestoreVaccineApplied {
    return FirestoreVaccineApplied(
        id = id,
        vaccineId = vaccineId,
        date = Timestamp(date.epochSecond, date.nano),
        name = name,
        description = description
    )
}