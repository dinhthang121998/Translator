package com.example.domain.base

import com.example.common.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(parameter: P): UiState<R> {
        return try {
            withContext(coroutineDispatcher) {
                UiState.Success(execute(parameter))
            }
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    protected abstract suspend fun execute(parameter: P): R
}
