package com.ganecamp.domain.usecase.weight

import com.ganecamp.domain.model.WeightValue
import com.ganecamp.domain.repository.WeightRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class LoadWeightValueUseCase @Inject constructor(private val weightRepository: WeightRepository) {
    suspend fun invoke(): OperationResult<WeightValue> {
        return weightRepository.loadWeightValue()
    }
}