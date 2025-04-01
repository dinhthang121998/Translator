package com.example.common

sealed class UiState<T>(
    val data: T?,
    val message: String = "",
) {
    class Loading<T>(data: T? = null) : UiState<T>(data = data)

    class Success<T>(data: T?) : UiState<T>(data = data)

    class Error<T>(data: T? = null, message: String) :
        UiState<T>(data = data, message = message)
}
