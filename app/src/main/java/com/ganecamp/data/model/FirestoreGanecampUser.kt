package com.ganecamp.data.model

import com.ganecamp.domain.model.GanecampUser
import com.google.firebase.firestore.Exclude

data class FirestoreGanecampUser(
    @get:Exclude @set:Exclude var id: String? = null,
    var email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val farmToken: String = ""
)

fun FirestoreGanecampUser.toGanecampUser(): GanecampUser {
    return GanecampUser(
        id = id, email = email, name = name, phoneNumber = phoneNumber, farmToken = farmToken
    )
}

fun GanecampUser.toFirestoreGanecampUser(): FirestoreGanecampUser {
    return FirestoreGanecampUser(
        id = id, email = email, name = name, phoneNumber = phoneNumber, farmToken = farmToken
    )
}