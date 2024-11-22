package com.ganecamp.domain.usecase.auth

import com.ganecamp.domain.auth.Auth
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val auth: Auth
) {
    suspend operator fun invoke(email: String, password: String): OperationResult<Unit> {
        return auth.signIn(email, password)
    }
}