package com.ganecamp.domain.usecase.auth

import com.ganecamp.domain.auth.Auth
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val auth: Auth
) {
    operator fun invoke() {
        auth.signOut()
    }
}