package com.example.data.repository

import com.example.model.TranslationHistory
import kotlinx.coroutines.flow.Flow

interface TranslationHistoryRepository {
    suspend fun addTranslatedWord(translatedWord: TranslationHistory)

    fun getAllTranslationHistory(): Flow<List<TranslationHistory>>

    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )

    suspend fun deleteTranslatedWord(id: Int)

    fun getAllFavoriteTranslationHistory(): Flow<List<TranslationHistory>>

    fun findTranslationByOriginalAndTranslated(
        original: String,
        translated: String,
        isDeleted: Boolean,
    ): TranslationHistory?
}
