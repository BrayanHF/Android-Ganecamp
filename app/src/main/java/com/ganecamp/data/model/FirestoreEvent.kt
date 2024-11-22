package com.ganecamp.data.model

import com.ganecamp.domain.model.Event
import com.google.firebase.firestore.Exclude

data class FirestoreEvent(
    @get:Exclude @set:Exclude var id: String? = null,
    val title: String = "",
    val description: String = ""
)

fun FirestoreEvent.toEvent(): Event {
    return Event(
        id = id, title = title, description = description
    )
}

fun Event.toFirestoreEvent(): FirestoreEvent {
    return FirestoreEvent(
        id = id, title = title, description = description
    )
}