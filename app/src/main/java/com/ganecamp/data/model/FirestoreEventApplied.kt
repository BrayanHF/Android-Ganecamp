package com.ganecamp.data.model

import com.ganecamp.domain.model.EventApplied
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class FirestoreEventApplied(
    @get:Exclude @set:Exclude var id: String? = null,
    val eventId: String = "",
    val date: Timestamp = Timestamp.now(),
    val title: String = "",
    val description: String = ""
)

fun FirestoreEventApplied.toEventApplied(): EventApplied {
    return EventApplied(
        id = id,
        eventId = eventId,
        date = date.toInstant(),
        title = title,
        description = description
    )
}

fun EventApplied.toFirestoreEventApplied(): FirestoreEventApplied {
    return FirestoreEventApplied(
        id = id,
        eventId = eventId,
        date = Timestamp(date.epochSecond, date.nano),
        title = title,
        description = description
    )
}