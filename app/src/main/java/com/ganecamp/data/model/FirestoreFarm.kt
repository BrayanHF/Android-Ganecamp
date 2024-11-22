package com.ganecamp.data.model

import com.ganecamp.domain.model.Farm
import com.google.firebase.firestore.Exclude

data class FirestoreFarm(
    @get:Exclude @set:Exclude var id: String? = null,
    val token: String = "",
    val location: String = "",
    val name: String = "",
    val owner: String = ""
)

fun FirestoreFarm.toFarm(): Farm {
    return Farm(
        id = id, token = token, location = location, name = name, owner = owner
    )
}

fun Farm.toFirestoreFarm(): FirestoreFarm {
    return FirestoreFarm(
        id = id, token = token, location = location, name = name, owner = owner
    )
}