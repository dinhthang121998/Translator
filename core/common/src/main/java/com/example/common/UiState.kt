package com.example.common

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val error: Exception) : UiState<Nothing>()

    data object Loading : UiState<Nothing>()
}
