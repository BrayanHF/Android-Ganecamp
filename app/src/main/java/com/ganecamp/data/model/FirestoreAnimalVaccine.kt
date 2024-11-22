package com.ganecamp.data.model

import com.ganecamp.domain.model.AnimalVaccine
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreAnimalVaccine(
    @get:Exclude @set:Exclude var id: String? = null,
    val vaccineId: String = "",
    val date: Timestamp = Timestamp.now()
)

fun FirestoreAnimalVaccine.toAnimalVaccine(): AnimalVaccine {
    return AnimalVaccine(
        id = id, vaccineId = vaccineId, date = date.toInstant()
    )
}

fun AnimalVaccine.toFirestoreAnimalVaccine(): FirestoreAnimalVaccine {
    return FirestoreAnimalVaccine(
        id = id, vaccineId = vaccineId, date = Timestamp(date)
    )
}