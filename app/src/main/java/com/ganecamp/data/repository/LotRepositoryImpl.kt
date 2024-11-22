package com.ganecamp.data.repository

import com.ganecamp.data.firestore.LotDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toAnimal
import com.ganecamp.data.model.toFirestoreLot
import com.ganecamp.data.model.toLot
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class LotRepositoryImpl @Inject constructor(
    private val dataSource: LotDataSource
) : LotRepository {

    override suspend fun getAllLots(): OperationResult<List<Lot>> {
        return when (val result = dataSource.getAllLots()) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toLot() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getLotById(id: String): OperationResult<Lot> {
        return when (val result = dataSource.getLotById(id)) {
            is DataResult.Success -> OperationResult.Success(result.data.toLot())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addLot(lot: Lot): OperationResult<String> {
        return when (val result = dataSource.addLot(lot.toFirestoreLot())) {
            is DataResult.Success -> OperationResult.Success(result.data)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateLot(lot: Lot): OperationResult<Unit> {
        return when (val result = dataSource.updateLot(lot.toFirestoreLot())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteLotById(id: String): OperationResult<Unit> {
        return when (val result = dataSource.deleteLotById(id)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getAnimalsByLotId(lotId: String): OperationResult<List<Animal>> {
        return when (val result = dataSource.getAnimalsByLotId(lotId)) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toAnimal() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}