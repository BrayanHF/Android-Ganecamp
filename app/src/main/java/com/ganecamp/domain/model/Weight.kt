package com.ganecamp.domain.model

import java.time.Instant

data class Weight(
    val id: String? = null,
    val weight: Float = 0f,
    val date: Instant = Instant.now()
)