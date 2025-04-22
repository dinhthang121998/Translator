package com.example.data.repository

import com.example.database.dao.TranslatedDao
import com.example.database.mapper.toDomain
import com.example.database.mapper.toEntity
import com.example.model.TranslationHistory
import com.example.network.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranslationHistoryRepositoryImpl
    @Inject
    constructor(private val translatedDao: TranslatedDao) :
    TranslationHistoryRepository {
        override suspend fun addTranslatedWord(translatedWord: TranslationHistory) {
            translatedDao.insertTranslationHistory(translatedWord.toEntity())
        }

        override fun getAllTranslationHistory(): Flow<List<TranslationHistory>> {
            return translatedDao.getAllTranslationHistory().map { it.map { it.toDomain() } }
        }

        override suspend fun updateTranslatedWordFavorite(
            id: Int,
            isFavorite: Boolean,
        ) {
            return translatedDao.updateTranslationHistoryFavorite(id, isFavorite)
        }

        override suspend fun deleteTranslationHistory(id: Int) {
            translatedDao.deleteTranslationHistory(id)
        }

        override fun getAllFavoriteTranslationHistory(): Flow<List<TranslationHistory>> {
            return translatedDao.getFavoredTranslationHistory().map {
                it.map { it.toDomain() }
            }
        }

        override fun findTranslationByOriginalAndTranslated(
            original: String,
            translated: String,
            isDeleted: Boolean,
        ): TranslationHistory? {
            return translatedDao.findTranslationByOriginalAndTranslated(original, translated, isDeleted)?.toDomain()
        }

    override suspend fun deleteAllTranslationHistory() {
        return translatedDao.deleteAllTranslationHistory()
    }
}
