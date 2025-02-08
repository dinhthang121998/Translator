package com.example.translator.data.repository

import com.example.translator.data.database.dao.TranslatedDao
import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val translatedDao: TranslatedDao) : HomeRepository {
    override suspend fun addTranslatedWord(translatedEntity: TranslatedEntity) {
        translatedDao.insertTranslatedWord(translatedEntity)
    }
}