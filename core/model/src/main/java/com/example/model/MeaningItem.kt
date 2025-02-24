package com.example.model

sealed class MeaningItem {
    data class PartOfSpeechItem(val partOfSpeech: String) : MeaningItem()

    data class DefinitionsItem(val definitions: Definitions) : MeaningItem()
}
