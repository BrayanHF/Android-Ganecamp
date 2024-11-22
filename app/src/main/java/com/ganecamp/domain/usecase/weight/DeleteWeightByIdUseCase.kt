package com.ganecamp.domain.usecase.weight

import com.ganecamp.domain.repository.WeightRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteWeightByIdUseCase @Inject constructor(
    private val repository: WeightRepository
) {
    suspend operator fun invoke(animalId: String, weightId: String): OperationResult<Unit> {
        return repository.deleteWeightById(animalId, weightId)
    }
}