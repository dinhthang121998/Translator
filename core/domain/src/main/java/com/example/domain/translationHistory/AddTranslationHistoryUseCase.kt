package com.example.domain.translationHistory

import android.util.Log
import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.TranslationHistory
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddTranslationHistoryUseCase
    @Inject
    constructor(
        private val translationHistoryRepository: TranslationHistoryRepository,
        coroutineDispatcher: CoroutineDispatcher,
    ) :
    SuspendUseCase<TranslationHistory, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: TranslationHistory) {
            val existedTranslatedWord =
                translationHistoryRepository.findTranslationByOriginalAndTranslated(
                    original = parameter.originalWord,
                    translated = parameter.translatedWord,
                    isDeleted = false,
                )
            Log.d("AAAA", "existedTranslatedWord = $existedTranslatedWord")

            val translated =
                TranslationHistory(
                    id = existedTranslatedWord?.id ?: 0,
                    originalWord = parameter.originalWord,
                    translatedWord = parameter.translatedWord,
                    isFavourite = existedTranslatedWord?.isFavourite ?: false,
                    createdAt = existedTranslatedWord?.createdAt ?: System.currentTimeMillis(),
                )

            translationHistoryRepository.addTranslatedWord(translated)
        }
    }
