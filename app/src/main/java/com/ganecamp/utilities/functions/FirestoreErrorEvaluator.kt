package com.ganecamp.utilities.functions


import com.ganecamp.utilities.enums.FirestoreRespond
import com.ganecamp.utilities.enums.FirestoreRespond.UNEXPECTED
import com.google.firebase.firestore.FirebaseFirestoreException

object FirestoreErrorEvaluator {

    fun evaluateError(exception: Exception): FirestoreRespond {
        return when (exception) {
            is FirebaseFirestoreException -> handleFirestoreException(exception)
            is NullPointerException -> FirestoreRespond.NULL_POINTER
            else -> FirestoreRespond.UNKNOWN
        }
    }

    private fun handleFirestoreException(exception: FirebaseFirestoreException): FirestoreRespond {
        return when (exception.code) {
            FirebaseFirestoreException.Code.CANCELLED -> FirestoreRespond.CANCELLED
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> FirestoreRespond.INVALID_ARGUMENT
            FirebaseFirestoreException.Code.NOT_FOUND -> FirestoreRespond.NOT_FOUND
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirestoreRespond.PERMISSION_DENIED
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> FirestoreRespond.UNAUTHENTICATED
            FirebaseFirestoreException.Code.ABORTED -> FirestoreRespond.ABORTED
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> FirestoreRespond.ALREADY_EXISTS
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> FirestoreRespond.FAILED_PRECONDITION
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> FirestoreRespond.OUT_OF_RANGE
            FirebaseFirestoreException.Code.UNAVAILABLE -> FirestoreRespond.UNAVAILABLE
            FirebaseFirestoreException.Code.DATA_LOSS -> FirestoreRespond.DATA_LOSS
            FirebaseFirestoreException.Code.UNKNOWN -> FirestoreRespond.UNKNOWN
            else -> UNEXPECTED
        }
    }

}