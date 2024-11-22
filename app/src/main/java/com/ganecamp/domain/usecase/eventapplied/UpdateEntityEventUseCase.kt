package com.ganecamp.domain.usecase.eventapplied

import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.EntityEvent
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class UpdateEntityEventUseCase @Inject constructor(
    private val eventAppliedRepository: EventAppliedRepository
) {
    suspend fun invoke(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<Unit> {
        return eventAppliedRepository.updateEntityEvent(entityId, entityEvent, entityType)
    }
}