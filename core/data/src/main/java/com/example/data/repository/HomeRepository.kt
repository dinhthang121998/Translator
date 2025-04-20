package com.example.data.repository

import com.example.model.TranslatedWord
import com.example.model.WordInformation
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun addTranslatedWord(translatedWord: TranslatedWord)

    fun getAllTranslatedWord(): Flow<List<TranslatedWord>>

    suspend fun getWordInformation(word: String): List<WordInformation>

    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )

    suspend fun deleteTranslatedWord(translatedWord: TranslatedWord)
}
