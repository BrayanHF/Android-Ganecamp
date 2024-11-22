package com.ganecamp.domain.usecase.eventapplied

import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.EntityEvent
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class AddEntityEventUseCase @Inject constructor(
    private val eventAppliedRepository: EventAppliedRepository
) {
    suspend fun invoke(
        entityId: String, entityEvent: EntityEvent, entityType: EntityType
    ): OperationResult<String> {
        return eventAppliedRepository.addEntityEvent(entityId, entityEvent, entityType)
    }
}