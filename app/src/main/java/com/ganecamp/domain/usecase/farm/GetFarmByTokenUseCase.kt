package com.ganecamp.domain.usecase.farm

import com.ganecamp.domain.model.Farm
import com.ganecamp.domain.repository.FarmRepository
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class GetFarmByTokenUseCase @Inject constructor(
    private val repository: FarmRepository
) {
    suspend operator fun invoke(token: String): OperationResult<Farm> {
        return repository.getFarmByToken(token)
    }
}