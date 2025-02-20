package com.example.translator.domain.repository

import com.example.translator.common.UiState
import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.data.remote.dto.WordInformationDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun addTranslatedWord(translatedEntity: TranslatedEntity)

    fun getAllTranslatedWord(): Flow<List<TranslatedEntity>>

    suspend fun getWordInformation(word: String): List<WordInformationDto>

    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )

    fun a(): Flow<UiState<List<TranslatedEntity>>>

    suspend fun b(translatedEntity: TranslatedEntity)
}
