package com.ganecamp.domain.usecase.vaccineapplied

import com.ganecamp.domain.model.AnimalVaccine
import com.ganecamp.domain.repository.VaccineAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class ApplyVaccineToAnimalUseCase @Inject constructor(
    private val repository: VaccineAppliedRepository
) {
    suspend operator fun invoke(
        animalId: String, animalVaccine: AnimalVaccine
    ): OperationResult<Unit> {
        return repository.applyVaccineToAnimal(animalId, animalVaccine)
    }
}