package com.ganecamp.data.firibase


import com.ganecamp.domain.enums.RepositoryRespond
import com.ganecamp.domain.enums.RepositoryRespond.UNEXPECTED
import com.google.firebase.firestore.FirebaseFirestoreException

object FirestoreErrorEvaluator {

    fun evaluateError(exception: Exception): RepositoryRespond {
        return when (exception) {
            is FirebaseFirestoreException -> handleFirestoreException(exception)
            is NullPointerException -> RepositoryRespond.NULL_POINTER
            else -> RepositoryRespond.UNKNOWN
        }
    }

    private fun handleFirestoreException(exception: FirebaseFirestoreException): RepositoryRespond {
        return when (exception.code) {
            FirebaseFirestoreException.Code.CANCELLED -> RepositoryRespond.CANCELLED
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> RepositoryRespond.INVALID_ARGUMENT
            FirebaseFirestoreException.Code.NOT_FOUND -> RepositoryRespond.NOT_FOUND
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> RepositoryRespond.PERMISSION_DENIED
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> RepositoryRespond.UNAUTHENTICATED
            FirebaseFirestoreException.Code.ABORTED -> RepositoryRespond.ABORTED
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> RepositoryRespond.ALREADY_EXISTS
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> RepositoryRespond.FAILED_PRECONDITION
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> RepositoryRespond.OUT_OF_RANGE
            FirebaseFirestoreException.Code.UNAVAILABLE -> RepositoryRespond.UNAVAILABLE
            FirebaseFirestoreException.Code.DATA_LOSS -> RepositoryRespond.DATA_LOSS
            FirebaseFirestoreException.Code.UNKNOWN -> RepositoryRespond.UNKNOWN
            else -> UNEXPECTED
        }
    }

}