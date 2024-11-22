package com.ganecamp.data.repository

import com.ganecamp.data.firestore.VaccineDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toFirestoreVaccine
import com.ganecamp.data.model.toVaccine
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.repository.VaccineRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class VaccineRepositoryImpl @Inject constructor(
    private val dataSource: VaccineDataSource
) : VaccineRepository {

    override suspend fun getAllVaccines(): OperationResult<List<Vaccine>> {
        return when (val result = dataSource.getAllVaccines()) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toVaccine() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun getVaccineById(id: String): OperationResult<Vaccine> {
        return when (val result = dataSource.getVaccineById(id)) {
            is DataResult.Success -> OperationResult.Success(result.data.toVaccine())
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun addVaccine(vaccine: Vaccine): OperationResult<String> {
        return when (val result = dataSource.addVaccine(vaccine.toFirestoreVaccine())) {
            is DataResult.Success -> OperationResult.Success(result.data)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateVaccine(vaccine: Vaccine): OperationResult<Unit> {
        return when (val result = dataSource.updateVaccine(vaccine.toFirestoreVaccine())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteVaccineById(id: String): OperationResult<Unit> {
        return when (val result = dataSource.deleteVaccineById(id)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}