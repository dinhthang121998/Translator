package com.example.model

data class Meanings(
    var partOfSpeech: String? = null,
    var definitions: List<Definitions> = listOf(),
)
