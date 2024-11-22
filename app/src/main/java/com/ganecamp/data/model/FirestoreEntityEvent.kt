package com.ganecamp.data.model

import com.ganecamp.domain.model.EntityEvent
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreEntityEvent(
    @get:Exclude @set:Exclude var id: String? = null,
    val eventId: String = "",
    val date: Timestamp = Timestamp.now()
)

fun FirestoreEntityEvent.toEntityEvent(): EntityEvent {
    return EntityEvent(
        id = id, eventId = eventId, date = date.toInstant()
    )
}

fun EntityEvent.toFirestoreEntityEvent(): FirestoreEntityEvent {
    return FirestoreEntityEvent(
        id = id, eventId = eventId, date = Timestamp(date)
    )
}