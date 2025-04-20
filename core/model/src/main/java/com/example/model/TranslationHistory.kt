package com.example.model

data class TranslationHistory(
    val id: Int = 0,
    val originalWord: String = "",
    val translatedWord: String = "",
    val isFavourite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
