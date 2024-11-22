package com.ganecamp.domain.usecase.farmsesion

import com.ganecamp.domain.enums.ErrorType
import com.ganecamp.domain.model.Farm
import com.ganecamp.domain.result.OperationResult
import com.ganecamp.domain.result.OperationResult.Error
import com.ganecamp.domain.result.OperationResult.Success
import com.ganecamp.domain.session.FarmSessionManager
import com.ganecamp.domain.usecase.farm.GetFarmByTokenUseCase
import com.ganecamp.domain.usecase.ganecampuser.GetUserByEmailUseCase
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoadFarmSessionUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val getFarmByTokenUseCase: GetFarmByTokenUseCase,
    private val farmSessionManager: FarmSessionManager
) {
    suspend fun invoke(): OperationResult<Farm> {
        val user = firebaseAuth.currentUser
        val userEmail = user?.email
        return userEmail?.let { email ->
            when (val userRespond = getUserByEmailUseCase(email)) {
                is Success -> loadFarm(userRespond.data.farmToken)
                is Error -> Error(userRespond.errorType)
            }
        } ?: Error(ErrorType.NOT_FOUND)
    }

    private suspend fun loadFarm(farmToken: String): OperationResult<Farm> {
        return when (val farmRespond = getFarmByTokenUseCase(farmToken)) {
            is Success -> {
                farmSessionManager.setFarm(farmRespond.data)
                Success(farmRespond.data)
            }

            is Error -> Error(farmRespond.errorType)
        }
    }

}