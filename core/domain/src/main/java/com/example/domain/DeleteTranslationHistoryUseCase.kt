package com.example.domain

import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTranslationHistoryUseCase
    @Inject
    constructor(
        private val translationHistoryRepository: TranslationHistoryRepository,
        coroutineDispatcher: CoroutineDispatcher,
    ) : SuspendUseCase<Int, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: Int) {
            translationHistoryRepository.deleteTranslatedWord(parameter)
        }
    }
