package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.model.WeightValue
import com.ganecamp.domain.result.OperationResult

interface WeightRepository {
    suspend fun getAnimalWeights(animalId: String): OperationResult<List<Weight>>
    suspend fun addWeight(animalId: String, weight: Weight): OperationResult<Unit>
    suspend fun updateWeight(animalId: String, weight: Weight): OperationResult<Unit>
    suspend fun deleteWeightById(animalId: String, weightId: String): OperationResult<Unit>
    suspend fun loadWeightValue(): OperationResult<WeightValue>
}