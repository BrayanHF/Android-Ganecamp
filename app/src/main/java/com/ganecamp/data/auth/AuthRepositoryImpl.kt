package com.ganecamp.data.auth

import com.ganecamp.data.mapper.ExceptionToErrorTypeMapper
import com.ganecamp.data.result.DataResult
import com.ganecamp.domain.auth.Auth
import com.ganecamp.domain.result.OperationResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
) : Auth {

    override suspend fun signIn(email: String, password: String): OperationResult<Unit> {
        return when (val result =
            firebaseAuthentication.signInWithEmailAndPassword(email, password)) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override suspend fun signUp(
        email: String, password: String, name: String, phoneNumber: String, farmToken: String
    ): OperationResult<Unit> {
        return when (val result = firebaseAuthentication.signUpWithEmailAndPassword(
            email, password, name, phoneNumber, farmToken
        )) {
            is DataResult.Success -> OperationResult.Success(Unit)
            is DataResult.Error -> OperationResult.Error(ExceptionToErrorTypeMapper.map(result.exception))
        }
    }

    override fun signOut() {
        firebaseAuthentication.signOut()
    }

}