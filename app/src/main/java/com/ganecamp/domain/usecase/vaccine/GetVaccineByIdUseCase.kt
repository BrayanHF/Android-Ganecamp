package com.ganecamp.domain.usecase.vaccine

import com.ganecamp.domain.model.Vaccine
import com.ganecamp.domain.repository.VaccineRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetVaccineByIdUseCase @Inject constructor(
    private val repository: VaccineRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Vaccine> {
        return repository.getVaccineById(id)
    }
}