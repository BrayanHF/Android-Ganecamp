package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.AnimalEventEntity
import com.ganecamp.data.database.entities.LotEventEntity
import java.time.ZonedDateTime

data class GeneralEvent(
    val id: Int,
    val entityId: Int,
    val eventId: Int,
    val applicationDate: ZonedDateTime
)

fun AnimalEventEntity.toDomain() = GeneralEvent(id, animalId, eventId, applicationDate)
fun LotEventEntity.toDomain() = GeneralEvent(id, lotId, eventId, applicationDate)