package com.ganecamp.data.repository

import com.ganecamp.data.firestore.EventAppliedDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toEventApplied
import com.ganecamp.data.model.toFirestoreEntityEvent
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.EntityEvent
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class EventAppliedRepositoryImpl @Inject constructor(
    private val dataSource: EventAppliedDataSource
) : EventAppliedRepository {

    override suspend fun getEntityEvents(
        entityId: String, entityType: EntityType
    ): OperationResult<List<EventApplied>> {
        return when (val result = dataSource.getEntityEvents(entityId, entityType)) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toEventApplied() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<String> {
        return when (val result =
            dataSource.addEntityEvent(entityId, entityEvent.toFirestoreEntityEvent(), entityType)) {
            is DataResult.Success -> OperationResult.Success(result.data)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateEntityEvent(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<Unit> {
        return when (val result = dataSource.updateEntityEvent(
            entityId, entityEvent.toFirestoreEntityEvent(), entityType
        )) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteEntityEventById(
        id: String, entityType: EntityType
    ): OperationResult<Unit> {
        return when (val result = dataSource.deleteEntityEventById(id, entityType)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}