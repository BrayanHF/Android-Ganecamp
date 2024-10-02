package com.ganecamp.model.objects

import com.ganecamp.model.interfaces.Applied
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class EventApplied(
    @get:Exclude @set:Exclude override var id: String? = null,
    val eventId: String = "",
    override val date: Timestamp = Timestamp.now(),
    val title: String = "",
    override val description: String = ""
) : Applied