package com.example.data.repository

import com.example.model.TranslationHistory
import com.example.model.WordInformation
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun addTranslatedWord(translatedWord: TranslationHistory)

    fun getAllTranslatedWord(): Flow<List<TranslationHistory>>

    suspend fun getWordInformation(word: String): List<WordInformation>

    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )

    suspend fun deleteTranslatedWord(translatedWord: TranslationHistory)
}
