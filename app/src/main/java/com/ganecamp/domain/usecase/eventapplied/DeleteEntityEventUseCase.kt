package com.ganecamp.domain.usecase.eventapplied

import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteEntityEventUseCase @Inject constructor(
    private val eventAppliedRepository: EventAppliedRepository
) {
    suspend fun invoke(id: String, entityType: EntityType): OperationResult<Unit> {
        return eventAppliedRepository.deleteEntityEventById(id, entityType)
    }
}