package com.ganecamp.domain.model

import java.time.Instant

data class AnimalVaccine(
    var id: String? = null, val vaccineId: String = "", val date: Instant = Instant.now()
)