package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.EventEntity
import com.ganecamp.data.model.DescriptionData
import java.time.ZonedDateTime

data class Description(
    val id: Int,
    val title: String,
    val date: ZonedDateTime,
    val description: String
)

fun DescriptionData.toDomain() = Description(id, title, date, description)