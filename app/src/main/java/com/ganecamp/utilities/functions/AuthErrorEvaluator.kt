package com.ganecamp.utilities.functions

import com.ganecamp.utilities.enums.AuthRespond
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException

object AuthErrorEvaluator {

    fun evaluateError(exception: Exception): AuthRespond {
        return when (exception) {
            is FirebaseAuthException -> handleAuthException(exception)
            is FirebaseNetworkException -> AuthRespond.NETWORK_ERROR
            else -> AuthRespond.UNEXPECTED
        }
    }

    private fun handleAuthException(exception: FirebaseAuthException): AuthRespond {
        return when (exception.errorCode) {
            "ERROR_USER_NOT_FOUND" -> AuthRespond.USER_NOT_FOUND
            "ERROR_EMAIL_ALREADY_IN_USE" -> AuthRespond.EMAIL_ALREADY_IN_USE
            "ERROR_INVALID_EMAIL" -> AuthRespond.INVALID_EMAIL
            "ERROR_WEAK_PASSWORD" -> AuthRespond.WEAK_PASSWORD
            "ERROR_USER_DISABLED" -> AuthRespond.USER_DISABLED
            else -> AuthRespond.UNKNOWN_ERROR
        }
    }
}