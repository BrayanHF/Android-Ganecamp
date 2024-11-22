package com.ganecamp.domain.usecase.eventapplied

import com.ganecamp.domain.enums.EntityType
import com.ganecamp.domain.model.EventApplied
import com.ganecamp.domain.repository.EventAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetEntityEventsUseCase @Inject constructor(
    private val eventAppliedRepository: EventAppliedRepository
) {
    suspend operator fun invoke(
        entityId: String, entityType: EntityType
    ): OperationResult<List<EventApplied>> {
        return eventAppliedRepository.getEntityEvents(entityId, entityType)
    }
}