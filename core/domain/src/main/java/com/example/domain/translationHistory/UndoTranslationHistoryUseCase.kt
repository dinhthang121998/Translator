package com.example.domain.translationHistory

import android.util.Log
import com.example.data.repository.TranslationHistoryRepository
import com.example.domain.base.SuspendUseCase
import com.example.model.TranslationHistory
import kotlinx.coroutines.CoroutineDispatcher

class UndoTranslationHistoryUseCase(
    private val translationHistoryRepository: TranslationHistoryRepository,
    coroutineDispatcher: CoroutineDispatcher,
) : SuspendUseCase<TranslationHistory, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameter: TranslationHistory) {
        // for Undo
        val existedButDeleted =
            translationHistoryRepository.findTranslationByOriginalAndTranslated(
                original = parameter.originalWord,
                translated = parameter.translatedWord,
                isDeleted = true,
            )

        Log.d("AAAA", "existedButDeleted: $existedButDeleted")

        val translated =
            TranslationHistory(
                id = existedButDeleted?.id ?: 0,
                originalWord = parameter.originalWord,
                translatedWord = parameter.translatedWord,
                isFavourite = existedButDeleted?.isFavourite ?: false,
                createdAt = existedButDeleted?.createdAt ?: System.currentTimeMillis(),
                updatedAt = existedButDeleted?.updatedAt ?: System.currentTimeMillis(),
            )

        translationHistoryRepository.addTranslatedWord(translated)
    }
}
