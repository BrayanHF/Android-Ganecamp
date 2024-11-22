package com.ganecamp.data.repository

import com.ganecamp.data.firestore.WeightDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toFirestoreWeight
import com.ganecamp.data.model.toWeight
import com.ganecamp.data.model.toWeightValue
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Weight
import com.ganecamp.domain.model.WeightValue
import com.ganecamp.domain.repository.WeightRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class WeightRepositoryImpl @Inject constructor(
    private val dataSource: WeightDataSource
) : WeightRepository {

    override suspend fun getAnimalWeights(animalId: String): OperationResult<List<Weight>> {
        return when (val result = dataSource.getAnimalWeights(animalId)) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toWeight() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addWeight(animalId: String, weight: Weight): OperationResult<Unit> {
        return when (val result = dataSource.addWeight(animalId, weight.toFirestoreWeight())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateWeight(animalId: String, weight: Weight): OperationResult<Unit> {
        return when (val result = dataSource.updateWeight(animalId, weight.toFirestoreWeight())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteWeightById(
        animalId: String, weightId: String
    ): OperationResult<Unit> {
        return when (val result = dataSource.deleteWeightById(weightId)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun loadWeightValue(): OperationResult<WeightValue> {
        return when (val result = dataSource.loadWeightValue()) {
            is DataResult.Success -> OperationResult.Success(result.data.toWeightValue())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}