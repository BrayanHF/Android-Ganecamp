package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.EventDao
import com.ganecamp.data.firibase.model.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventService @Inject constructor(private val eventDao: EventDao) {

    suspend fun getAllEvents() = eventDao.getAllEvents()

    suspend fun getEventById(id: String) = eventDao.getEventById(id)

    suspend fun createEvent(event: Event) = eventDao.createEvent(event)

    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)

    suspend fun deleteEventById(id: String) = eventDao.deleteEventById(id)

}