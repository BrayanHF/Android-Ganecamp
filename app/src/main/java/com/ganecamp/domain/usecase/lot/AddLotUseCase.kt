package com.ganecamp.domain.usecase.lot

import com.ganecamp.domain.model.Lot
import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class AddLotUseCase @Inject constructor(
    private val repository: LotRepository
) {
    suspend operator fun invoke(lot: Lot): OperationResult<String> {
        return repository.addLot(lot)
    }
}