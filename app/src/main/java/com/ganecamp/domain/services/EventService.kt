package com.ganecamp.domain.services

import com.ganecamp.data.database.dao.EventDao
import com.ganecamp.data.database.entities.toEntity
import com.ganecamp.domain.model.Description
import com.ganecamp.domain.model.Event
import com.ganecamp.domain.model.toDomain
import javax.inject.Inject

class EventService @Inject constructor(private val eventDao: EventDao) {

    suspend fun getAllEvents(): List<Event> {
        return eventDao.getAllEvents().map { eventEntity ->
            eventEntity.toDomain()
        }
    }

    suspend fun getEventById(id: Int): Event = eventDao.getEventById(id).toDomain()

    suspend fun insertEvent(event: Event) = eventDao.insertEvent(event.toEntity())

    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event.toEntity())

    suspend fun deleteEventById(id: Int) = eventDao.deleteEventById(id)

    suspend fun deleteAllEvents() = eventDao.deleteAllEvents()

    suspend fun animalEvents(animalId: Int): List<Description> {
        return eventDao.animalEvents(animalId).map { descriptionData ->
            descriptionData.toDomain()
        }
    }

    suspend fun lotEvents(lotId: Int): List<Description> {
        return eventDao.lotEvents(lotId).map { descriptionData ->
            descriptionData.toDomain()
        }
    }
}
