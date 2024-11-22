package com.ganecamp.domain.usecase.auth

import com.ganecamp.domain.auth.Auth
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val auth: Auth
) {
    suspend operator fun invoke(
        email: String, password: String, name: String, phoneNumber: String, farmToken: String
    ): OperationResult<Unit> {
        return auth.signUp(email, password, name, phoneNumber, farmToken)
    }
}