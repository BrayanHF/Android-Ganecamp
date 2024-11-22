package com.ganecamp.domain.model

import java.time.Instant

data class WeightValue(
    val value: Float = 0f, val date: Instant = Instant.now()
)