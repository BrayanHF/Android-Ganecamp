package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.EventEntity

data class Event(
    val id: Int,
    val title: String,
    val description: String
)

fun EventEntity.toDomain() = Event(id, title, description)