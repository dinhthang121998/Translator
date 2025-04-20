package com.example.data.repository

import com.example.database.dao.TranslatedDao
import com.example.database.mapper.toDomain
import com.example.database.mapper.toEntity
import com.example.model.TranslationHistory
import com.example.model.WordInformation
import com.example.network.ApiService
import com.example.network.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl
    @Inject
    constructor(private val translatedDao: TranslatedDao, private val apiService: ApiService) :
    HomeRepository {
        override suspend fun addTranslatedWord(translatedWord: TranslationHistory) {
            translatedDao.insertTranslationHistory(translatedWord.toEntity())
        }

        override fun getAllTranslatedWord(): Flow<List<TranslationHistory>> {
            return translatedDao.getAllTranslationHistory().map { it.map { it.toDomain() } }
        }

        override suspend fun getWordInformation(word: String): List<WordInformation> {
            return apiService.getWordInformation(word).map { it.toDomain() }
        }

        override suspend fun updateTranslatedWordFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            return translatedDao.updateTranslationHistoryFavorite(id, isFavorite)
        }

        override suspend fun deleteTranslatedWord(translatedWord: TranslationHistory) {
            translatedDao.deleteTranslationHistory(translatedWord.toEntity())
        }
    }
