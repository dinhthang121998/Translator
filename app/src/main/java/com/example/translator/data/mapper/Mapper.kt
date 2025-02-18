package com.example.translator.data.mapper

import com.example.translator.LanguageItemStore
import com.example.translator.data.database.entity.TranslatedEntity
import com.example.translator.data.remote.dto.DefinitionsDto
import com.example.translator.data.remote.dto.MeaningsDto
import com.example.translator.data.remote.dto.WordInformationDto
import com.example.translator.domain.model.Definitions
import com.example.translator.domain.model.Downloadable
import com.example.translator.domain.model.Meanings
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.domain.model.TranslatedWord
import com.example.translator.domain.model.WordInformation

fun TranslatedWord.toTranslatedEntity(): TranslatedEntity {
    return TranslatedEntity(
        originalWord = this.originalWord,
        translatedWord = this.translatedWord,
        isFavourite = this.isFavourite,
    )
}

fun LanguageItemStore.toLanguageItem() =
    SearchLanguageItem.LanguageItem(
        this.languageName,
        this.languageCode,
        Downloadable.fromId(this.downloaded),
    )

fun SearchLanguageItem.LanguageItem.toLanguageItemStore() =
    LanguageItemStore.newBuilder().apply {
        setLanguageName(this@toLanguageItemStore.languageName)
        setLanguageCode(this@toLanguageItemStore.languageCode)
        setDownloaded(this@toLanguageItemStore.downloadable.id)
    }

fun ArrayList<DefinitionsDto>.toDefinitions(): ArrayList<Definitions> {
    return this.map { definition ->
        Definitions(definition.definition, definition.example, definition.synonyms, definition.antonyms)
    } as ArrayList<Definitions>
}

fun ArrayList<MeaningsDto>.toMeanings(): ArrayList<Meanings> {
    return this.map { meaning ->
        Meanings(meaning.partOfSpeech, meaning.definitions.toDefinitions())
    } as ArrayList<Meanings>
}

fun WordInformationDto.toWordInformation() =
    WordInformation(
        word = this.word,
        phonetic = this.phonetic,
        meaning = this.meanings.toMeanings(),
    )
