package com.ganecamp.domain.result

import com.ganecamp.domain.enums.ErrorType

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Error(val errorType: ErrorType) : OperationResult<Nothing>()
}