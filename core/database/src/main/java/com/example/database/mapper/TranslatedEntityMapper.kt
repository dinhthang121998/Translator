package com.example.database.mapper

import com.example.database.model.TranslatedEntity
import com.example.model.TranslatedWord

fun TranslatedEntity.toDomain(): TranslatedWord {
    return TranslatedWord(
        id,
        originalWord,
        translatedWord,
        isFavourite,
        createdAt,
        updatedAt,
    )
}

fun TranslatedWord.toEntity(): TranslatedEntity {
    return TranslatedEntity(
        id = id,
        originalWord = originalWord,
        translatedWord = translatedWord,
        isFavourite = isFavourite,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
