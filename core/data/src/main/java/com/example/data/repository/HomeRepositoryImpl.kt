package com.example.data.repository

import com.example.database.dao.TranslatedDao
import com.example.database.model.TranslatedEntity
import com.example.network.ApiService
import com.example.network.dto.WordInformationDto
import kotlinx.coroutines.flow.Flow
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
    }
