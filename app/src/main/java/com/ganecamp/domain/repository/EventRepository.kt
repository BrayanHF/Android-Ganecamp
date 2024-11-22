package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Event
import com.ganecamp.domain.result.OperationResult

interface EventRepository {
    suspend fun getAllFarmEvents(): OperationResult<List<Event>>
    suspend fun getFarmEventById(id: String): OperationResult<Event>
    suspend fun addFarmEvent(event: Event): OperationResult<String>
    suspend fun updateFarmEvent(event: Event): OperationResult<Unit>
    suspend fun deleteFarmEventById(id: String): OperationResult<Unit>
}