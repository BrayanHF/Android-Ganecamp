package com.ganecamp.domain.usecase.lot

import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetAllLotsUseCase @Inject constructor(
    private val repository: LotRepository
) {
    suspend operator fun invoke(): OperationResult<List<Lot>> {
        return repository.getAllLots()
    }
}