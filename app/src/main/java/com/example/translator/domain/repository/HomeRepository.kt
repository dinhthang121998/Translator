package com.example.translator.domain.repository

import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.data.remote.dto.WordInformationDto

interface HomeRepository {
    suspend fun addTranslatedWord(translatedEntity: TranslatedEntity)

    suspend fun getWordInformation(word: String): List<WordInformationDto>
}
