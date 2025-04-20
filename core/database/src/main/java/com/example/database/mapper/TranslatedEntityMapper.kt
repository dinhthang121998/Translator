package com.example.database.mapper

import com.example.database.model.TranslationHistoryEntity
import com.example.model.TranslationHistory

fun TranslationHistoryEntity.toDomain(): TranslationHistory {
    return TranslationHistory(
        id,
        originalWord,
        translatedWord,
        isFavourite,
        createdAt,
        updatedAt,
    )
}

fun TranslationHistory.toEntity(): TranslationHistoryEntity {
    return TranslationHistoryEntity(
        id = id,
        originalWord = originalWord,
        translatedWord = translatedWord,
        isFavourite = isFavourite,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
