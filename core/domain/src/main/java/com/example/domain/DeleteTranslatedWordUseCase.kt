package com.example.domain

import com.example.data.repository.HomeRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.TranslationHistory
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTranslatedWordUseCase
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
        coroutineDispatcher: CoroutineDispatcher,
    ) : SuspendUseCase<TranslationHistory, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: TranslationHistory) {
            homeRepository.deleteTranslatedWord(parameter)
        }
    }
