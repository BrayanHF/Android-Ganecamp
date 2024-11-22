package com.ganecamp.data.repository

import com.ganecamp.data.firestore.EventDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toEvent
import com.ganecamp.data.model.toFirestoreEvent
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Event
import com.ganecamp.domain.repository.EventRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dataSource: EventDataSource
) : EventRepository {

    override suspend fun getAllFarmEvents(): OperationResult<List<Event>> {
        return when (val result = dataSource.getAllFarmEvents()) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toEvent() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getFarmEventById(id: String): OperationResult<Event> {
        return when (val result = dataSource.getFarmEventById(id)) {
            is DataResult.Success -> OperationResult.Success(result.data.toEvent())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addFarmEvent(event: Event): OperationResult<String> {
        return when (val result = dataSource.addFarmEvent(event.toFirestoreEvent())) {
            is DataResult.Success -> OperationResult.Success(result.data)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateFarmEvent(event: Event): OperationResult<Unit> {
        return when (val result = dataSource.updateFarmEvent(event.toFirestoreEvent())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteFarmEventById(id: String): OperationResult<Unit> {
        return when (val result = dataSource.deleteFarmEventById(id)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}