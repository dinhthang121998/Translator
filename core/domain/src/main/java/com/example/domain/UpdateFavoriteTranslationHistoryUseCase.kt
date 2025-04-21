package com.example.domain

import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.TranslationHistory
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateFavoriteTranslationHistoryUseCase
    @Inject
    constructor(private val translationHistoryRepository: TranslationHistoryRepository, coroutineDispatcher: CoroutineDispatcher) :
    SuspendUseCase<TranslationHistory, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: TranslationHistory) {
            translationHistoryRepository.updateTranslatedWordFavorite(parameter.id, !parameter.isFavourite)
        }
    }
