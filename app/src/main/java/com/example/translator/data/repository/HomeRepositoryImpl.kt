package com.example.translator.data.repository

import com.example.translator.common.UiState
import com.example.translator.data.database.dao.TranslatedDao
import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.data.remote.api.ApiService
import com.example.translator.data.remote.dto.WordInformationDto
import com.example.translator.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl
    @Inject
    constructor(private val translatedDao: TranslatedDao, private val apiService: ApiService) :
    HomeRepository {
        override suspend fun addTranslatedWord(translatedEntity: TranslatedEntity) {
            translatedDao.insertTranslatedWord(translatedEntity)
        }

        override fun getAllTranslatedWord(): Flow<List<TranslatedEntity>> {
            return translatedDao.getAllTranslatedWord()
        }

        override suspend fun getWordInformation(word: String): List<WordInformationDto> {
            return apiService.getWordInformation(word)
        }

        override suspend fun updateTranslatedWordFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            return translatedDao.updateTranslatedWordFavorite(id, isFavorite)
        }

        override fun a() = translatedDao.getAllTranslatedWord().map { UiState.Success(it) }

        override suspend fun b(translatedEntity: TranslatedEntity) {
            translatedDao.b(translatedEntity)
        }
    }
