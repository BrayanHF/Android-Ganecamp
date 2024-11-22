package com.ganecamp.domain.usecase.vaccineapplied

import com.ganecamp.domain.repository.VaccineAppliedRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteVaccineAppliedUseCase @Inject constructor(
    private val repository: VaccineAppliedRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Unit> {
        return repository.deleteVaccineApplied(id)
    }
}