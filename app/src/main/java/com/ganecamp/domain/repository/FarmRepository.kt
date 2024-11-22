package com.ganecamp.domain.repository

import com.ganecamp.domain.model.Farm
import com.ganecamp.domain.result.OperationResult

interface FarmRepository {
    suspend fun getFarmByToken(token: String): OperationResult<Farm>
    suspend fun updateFarm(farm: Farm): OperationResult<Unit>
}