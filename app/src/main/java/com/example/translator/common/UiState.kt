package com.example.translator.common

sealed class UiState<T>(
    data: T? = null,
    message: String = ""
) {
    class Loading<T>(): UiState<T>()
    class Success<T>(val data: T?): UiState<T>(data = data)
    class Error<T>(val message: String): UiState<T>(message = message)
}