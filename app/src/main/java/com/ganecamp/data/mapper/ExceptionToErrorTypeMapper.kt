package com.ganecamp.data.mapper

import com.ganecamp.data.error.DataError
import com.ganecamp.domain.enums.ErrorType
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException

object ExceptionToErrorTypeMapper {

    fun map(exception: Throwable): ErrorType {
        return when (exception) {
            is FirebaseAuthException -> mapAuthException(exception)
            is FirebaseFirestoreException -> mapFirestoreException(exception)
            is FirebaseNetworkException -> ErrorType.UNAVAILABLE
            is NullPointerException -> ErrorType.NULL_POINTER
            is DataError -> mapCustomError(exception)
            else -> ErrorType.UNKNOWN
        }
    }

    private fun mapCustomError(exception: DataError): ErrorType {
        return when (exception) {
            is DataError.NoFarmSessionError -> ErrorType.NO_FARM_SESSION
            is DataError.NotFoundError -> ErrorType.NOT_FOUND
        }
    }

    private fun mapAuthException(exception: FirebaseAuthException): ErrorType {
        return when (exception.errorCode) {
            "ERROR_USER_NOT_FOUND" -> ErrorType.USER_NOT_FOUND
            "ERROR_EMAIL_ALREADY_IN_USE" -> ErrorType.EMAIL_ALREADY_IN_USE
            "ERROR_INVALID_EMAIL" -> ErrorType.INVALID_EMAIL
            "ERROR_USER_DISABLED" -> ErrorType.USER_DISABLED
            else -> ErrorType.UNKNOWN
        }
    }

    private fun mapFirestoreException(exception: FirebaseFirestoreException): ErrorType {
        return when (exception.code) {
            FirebaseFirestoreException.Code.CANCELLED -> ErrorType.CANCELLED
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> ErrorType.INVALID_ARGUMENT
            FirebaseFirestoreException.Code.NOT_FOUND -> ErrorType.NOT_FOUND
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> ErrorType.PERMISSION_DENIED
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> ErrorType.UNAUTHENTICATED
            FirebaseFirestoreException.Code.ABORTED -> ErrorType.ABORTED
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> ErrorType.ALREADY_EXISTS
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> ErrorType.FAILED_PRECONDITION
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> ErrorType.OUT_OF_RANGE
            FirebaseFirestoreException.Code.UNAVAILABLE -> ErrorType.UNAVAILABLE
            FirebaseFirestoreException.Code.DATA_LOSS -> ErrorType.DATA_LOSS
            FirebaseFirestoreException.Code.UNKNOWN -> ErrorType.UNKNOWN
            else -> ErrorType.UNEXPECTED
        }
    }
}
