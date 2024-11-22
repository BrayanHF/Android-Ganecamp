package com.ganecamp.domain.usecase.lot

import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class DeleteLotByIdUseCase @Inject constructor(
    private val repository: LotRepository
) {
    suspend operator fun invoke(id: String): OperationResult<Unit> {
        return repository.deleteLotById(id)
    }
}