package com.ganecamp.domain.repository

import com.ganecamp.domain.model.AnimalVaccine
import com.ganecamp.domain.model.VaccineApplied
import com.ganecamp.domain.result.OperationResult

interface VaccineAppliedRepository {
    suspend fun getVaccinesApplied(animalId: String): OperationResult<List<VaccineApplied>>

    suspend fun applyVaccineToAnimal(
        animalId: String, animalVaccine: AnimalVaccine
    ): OperationResult<Unit>

    suspend fun updateVaccineApplied(
        animalId: String, animalVaccine: AnimalVaccine
    ): OperationResult<Unit>

    suspend fun deleteVaccineApplied(id: String): OperationResult<Unit>
}