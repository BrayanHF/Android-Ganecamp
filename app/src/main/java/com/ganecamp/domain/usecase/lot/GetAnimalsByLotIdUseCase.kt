package com.ganecamp.domain.usecase.lot

import com.ganecamp.domain.model.Animal
import com.ganecamp.domain.repository.LotRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetAnimalsByLotIdUseCase @Inject constructor(
    private val repository: LotRepository
) {
    suspend operator fun invoke(lotId: String): OperationResult<List<Animal>> {
        return repository.getAnimalsByLotId(lotId)
    }
}