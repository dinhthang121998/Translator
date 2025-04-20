package com.example.model

data class WordInformation(
    var word: String? = null,
    var phonetic: String? = null,
    var meanings: List<Meanings> = listOf(),
)
