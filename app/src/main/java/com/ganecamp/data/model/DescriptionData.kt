package com.ganecamp.data.model

import java.time.ZonedDateTime

data class DescriptionData(
    val id: Int,
    val title: String,
    val date: ZonedDateTime,
    val description: String
)