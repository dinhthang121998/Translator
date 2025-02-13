package com.example.translator.data.mapper

import com.example.translator.LanguageItemStore
import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.domain.model.Downloadable
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord

fun TranslatedWord.toTranslatedEntity(): TranslatedEntity {
    return TranslatedEntity(
        originalWord = this.originalWord,
        translatedWord = this.translatedWord,
        isFavourite = this.isFavourite,
    )
}

fun LanguageItemStore.toLanguageItem() =
    Downloadable.fromId(this.downloaded)
        ?.let { SearchLanguageItem.LanguageItem(this.languageName, this.languageCode, it) }

fun SearchLanguageItem.LanguageItem.toLanguageItemStore() = LanguageItemStore.newBuilder().apply {
    setLanguageName(this@toLanguageItemStore.languageName)
    setLanguageCode(this@toLanguageItemStore.languageCode)
    setDownloaded(this@toLanguageItemStore.downloadable.id)
}
