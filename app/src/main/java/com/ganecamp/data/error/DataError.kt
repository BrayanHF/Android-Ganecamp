package com.ganecamp.data.error

sealed class DataError: Throwable() {
    class NoFarmSessionError : DataError()
    class NotFoundError : DataError()
}