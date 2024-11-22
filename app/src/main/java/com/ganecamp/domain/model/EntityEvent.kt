package com.ganecamp.domain.model

import java.time.Instant

data class EntityEvent(
    val id: String? = null,
    val eventId: String = "",
    val date: Instant = Instant.now()
)