package com.example.data.repository

import com.example.database.model.TranslatedEntity
import com.example.network.dto.WordInformationDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun addTranslatedWord(translatedEntity: TranslatedEntity)

    fun getAllTranslatedWord(): Flow<List<TranslatedEntity>>

    suspend fun getWordInformation(word: String): List<WordInformationDto>

    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )
}
