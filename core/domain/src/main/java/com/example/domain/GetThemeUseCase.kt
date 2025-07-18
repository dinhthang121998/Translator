package com.example.domain

import com.example.common.UiState
import com.example.datastore.DatastorePrefManager
import com.example.domain.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetThemeUseCase(
    private val datastorePrefManager: DatastorePrefManager,
    coroutineDispatcher: CoroutineDispatcher,
) :
    FlowUseCase<Unit, Boolean?>(coroutineDispatcher) {
    override fun execute(parameter: Unit): Flow<UiState<Boolean?>> {
        return datastorePrefManager.getTheme().map { UiState.Success(it) }
    }
}
