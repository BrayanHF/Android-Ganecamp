package com.ganecamp.data.repository

import com.ganecamp.data.firestore.FarmDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toFarm
import com.ganecamp.data.model.toFirestoreFarm
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Farm
import com.ganecamp.domain.repository.FarmRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class FarmRepositoryImpl @Inject constructor(
    private val dataSource: FarmDataSource
) : FarmRepository {

    override suspend fun getFarmByToken(token: String): OperationResult<Farm> {
        return when (val result = dataSource.getFarmByToken(token)) {
            is DataResult.Success -> OperationResult.Success(result.data.toFarm())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateFarm(farm: Farm): OperationResult<Unit> {
        return when (val result = dataSource.updateFarm(farm.toFirestoreFarm())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}