package com.example.domain

import com.example.data.repository.HomeRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.TranslatedWord
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTranslatedWordUseCase
    @Inject
    constructor(
        private val homeRepository: HomeRepository,
        coroutineDispatcher: CoroutineDispatcher,
    ) : SuspendUseCase<TranslatedWord, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: TranslatedWord) {
            homeRepository.deleteTranslatedWord(parameter)
        }
    }
