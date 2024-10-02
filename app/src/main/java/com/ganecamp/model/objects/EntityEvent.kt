package com.ganecamp.model.objects

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class EntityEvent(
    @get:Exclude @set:Exclude var id: String? = null,
    val eventId: String = "",
    val date: Timestamp = Timestamp.now()
)