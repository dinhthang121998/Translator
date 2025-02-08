package com.example.translator.domain.repository

import com.example.translator.data.database.entity.TranslatedEntity

interface HomeRepository {

    suspend fun addTranslatedWord(translatedEntity: TranslatedEntity)

}