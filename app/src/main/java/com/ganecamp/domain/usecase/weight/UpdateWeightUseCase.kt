package com.ganecamp.domain.usecase.weight

import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.repository.WeightRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class UpdateWeightUseCase @Inject constructor(
    private val repository: WeightRepository
) {
    suspend operator fun invoke(animalId: String, weight: Weight): OperationResult<Unit> {
        return repository.updateWeight(animalId, weight)
    }
}