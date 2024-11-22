package com.ganecamp.domain.usecase.event

import com.ganecamp.domain.model.Event
import com.ganecamp.domain.repository.EventRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class AddFarmEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event): OperationResult<String> {
        return repository.addFarmEvent(event)
    }
}