package com.ganecamp.domain.usecase.event

import com.ganecamp.domain.repository.EventRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteFarmEventByIdUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Unit> {
        return repository.deleteFarmEventById(id)
    }
}