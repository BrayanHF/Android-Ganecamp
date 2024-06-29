package com.ganecamp.domain.model

import java.time.ZonedDateTime

data class Description(
    val title: String,
    val date: ZonedDateTime,
    val description: String
)