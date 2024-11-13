package com.ganecamp.domain.services

import com.ganecamp.data.firibase.dao.EventAppliedDao
import com.ganecamp.data.firibase.model.EntityEvent
import com.ganecamp.utilities.enums.EntityType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAppliedService @Inject constructor(private val eventAppliedDao: EventAppliedDao) {

    suspend fun getEntityEvents(entityId: String, entityType: EntityType) =
        eventAppliedDao.getEntityEvents(entityId, entityType)

    suspend fun createEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ) = eventAppliedDao.createEntityEvent(entityId, entityEvent, entityType)

    suspend fun updateEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ) = eventAppliedDao.updateEntityEvent(entityId, entityEvent, entityType)

    suspend fun deleteEntityEventById(id: String, entityType: EntityType) =
        eventAppliedDao.deleteEntityEventById(id, entityType)

}