package com.example.domain.base

import com.example.common.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(parameter: P): Flow<UiState<R>> =
        execute(parameter)
            .catch { e -> emit(UiState.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameter: P): Flow<UiState<R>>
}
