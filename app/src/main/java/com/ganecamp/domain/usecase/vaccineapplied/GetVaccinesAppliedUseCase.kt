package com.ganecamp.domain.usecase.vaccineapplied

import com.ganecamp.domain.model.VaccineApplied
import com.ganecamp.domain.repository.VaccineAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetVaccinesAppliedUseCase @Inject constructor(
    private val repository: VaccineAppliedRepository
) {
    suspend operator fun invoke(animalId: String): OperationResult<List<VaccineApplied>> {
        return repository.getVaccinesApplied(animalId)
    }
}