package com.ganecamp.domain.auth

import com.ganecamp.domain.result.OperationResult

interface Auth {

    suspend fun signIn(email: String, password: String): OperationResult<Unit>

    suspend fun signUp(
        email: String, password: String, name: String, phoneNumber: String, farmToken: String
    ): OperationResult<Unit>

    fun signOut()
}