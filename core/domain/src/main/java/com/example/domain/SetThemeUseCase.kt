package com.example.domain

import com.example.datastore.DatastorePrefManager
import com.example.domain.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SetThemeUseCase(
    private val datastorePrefManager: DatastorePrefManager,
    coroutineDispatcher: CoroutineDispatcher,
) :
    SuspendUseCase<Boolean, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Boolean) {
        datastorePrefManager.saveTheme(parameter)
    }
}
