package com.ganecamp.domain.repository

import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.EntityEvent
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.result.OperationResult

interface EventAppliedRepository {
    suspend fun getEntityEvents(
        entityId: String, entityType: EntityType
    ): OperationResult<List<EventApplied>>

    suspend fun addEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<String>

    suspend fun updateEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<Unit>

    suspend fun deleteEntityEventById(id: String, entityType: EntityType): OperationResult<Unit>
}