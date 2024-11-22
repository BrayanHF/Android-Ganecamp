package com.ganecamp.domain.usecase.farm

import com.ganecamp.domain.model.Farm
import com.ganecamp.domain.repository.FarmRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class UpdateFarmUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    suspend operator fun invoke(farm: Farm): OperationResult<Unit> {
        return repository.updateFarm(farm)
    }
}