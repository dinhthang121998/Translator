package com.example.domain

import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher

class DeleteAllTranslationHistoryUseCase(
    private val translationHistoryRepository: TranslationHistoryRepository,
    coroutineDispatcher: CoroutineDispatcher
): SuspendUseCase<Unit, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: Unit) {
        translationHistoryRepository.deleteAllTranslationHistory()
    }
}