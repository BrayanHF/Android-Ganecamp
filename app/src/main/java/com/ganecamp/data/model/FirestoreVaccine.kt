package com.ganecamp.data.model

import com.ganecamp.domain.model.Vaccine
import com.google.firebase.firestore.Exclude

data class FirestoreVaccine(
    @get:Exclude @set:Exclude var id: String? = null,
    val name: String = "",
    val description: String = ""
)

fun FirestoreVaccine.toVaccine(): Vaccine {
    return Vaccine(
        id = id, name = name, description = description
    )
}

fun Vaccine.toFirestoreVaccine(): FirestoreVaccine {
    return FirestoreVaccine(
        id = id, name = name, description = description
    )

}