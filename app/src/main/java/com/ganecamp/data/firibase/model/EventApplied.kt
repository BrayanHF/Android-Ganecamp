package com.ganecamp.data.firibase.model

import com.ganecamp.domain.model.interfaces.Applied
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

// Todo: Applied in domain
data class EventApplied(
    @get:Exclude @set:Exclude override var id: String? = null,
    val eventId: String = "",
    override val date: Timestamp = Timestamp.now(),
    val title: String = "",
    override val description: String = ""
) : Applied