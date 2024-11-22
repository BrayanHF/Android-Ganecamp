package com.ganecamp.data.repository

import com.ganecamp.data.firestore.VaccineAppliedDataSource
import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.model.toFirestoreAnimalVaccine
import com.ganecamp.data.model.toVaccineApplied
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.model.AnimalVaccine
import com.ganecamp.domain.model.VaccineApplied
import com.ganecamp.domain.repository.VaccineAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class VaccineAppliedRepositoryImpl @Inject constructor(
    private val dataSource: VaccineAppliedDataSource
) : VaccineAppliedRepository {

    override suspend fun getVaccinesApplied(animalId: String): OperationResult<List<VaccineApplied>> {
        return when (val result = dataSource.getVaccinesApplied(animalId)) {
            is DataResult.Success -> OperationResult.Success(result.data.map { it.toVaccineApplied() })
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun applyVaccineToAnimal(
        animalId: String, animalVaccine: AnimalVaccine
    ): OperationResult<Unit> {
        return when (val result =
            dataSource.applyVaccineAnimal(animalId, animalVaccine.toFirestoreAnimalVaccine())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun updateVaccineApplied(
        animalId: String, animalVaccine: AnimalVaccine
    ): OperationResult<Unit> {
        return when (val result =
            dataSource.updateVaccineApplied(animalId, animalVaccine.toFirestoreAnimalVaccine())) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun deleteVaccineApplied(id: String): OperationResult<Unit> {
        return when (val result = dataSource.deleteVaccineApplied(id)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

}