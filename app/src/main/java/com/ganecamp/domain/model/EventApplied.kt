package com.ganecamp.domain.model

import java.time.Instant

data class EventApplied(
    val id: String? = null,
    val eventId: String = "",
    val date: Instant = Instant.now(),
    val title: String = "",
    val description: String = ""
)