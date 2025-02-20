package com.example.translator.domain.model

data class TranslatedWord(
    val id: Int = 0,
    val originalWord: String = "",
    val translatedWord: String = "",
    val isFavourite: Boolean = false,
)
