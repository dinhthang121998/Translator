package com.example.translator.domain.model

data class TranslatedWord(
    val originalWord: String = "",
    val translatedWord: String = "",
    val isFavourite: Boolean = false
) {

}