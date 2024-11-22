package com.ganecamp.domain.model

import java.time.Instant

data class VaccineApplied(
    val id: String? = null,
    val vaccineId: String = "",
    val date: Instant = Instant.now(),
    val name: String = "",
    val description: String = ""
)