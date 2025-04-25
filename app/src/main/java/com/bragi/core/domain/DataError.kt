package com.bragi.core.domain

sealed interface DataError: Error {
    enum class Network: DataError {
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN
    }
}