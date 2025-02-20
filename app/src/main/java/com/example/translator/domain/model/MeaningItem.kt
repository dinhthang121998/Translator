package com.example.translator.domain.model

sealed class MeaningItem {
    data class PartOfSpeechItem(val partOfSpeech: String) : MeaningItem()

    data class DefinitionsItem(val definitions: Definitions) : MeaningItem()
}
