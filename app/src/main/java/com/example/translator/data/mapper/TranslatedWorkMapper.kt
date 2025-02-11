package com.example.translator.data.mapper

import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.domain.model.TranslatedWord

fun TranslatedWord.toTranslatedEntity(): TranslatedEntity {
    return TranslatedEntity(
        originalWord = this.originalWord,
        translatedWord = this.translatedWord,
        isFavourite = this.isFavourite,
    )
}
