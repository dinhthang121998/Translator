package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranslationHistoryEntity(
    val originalWord: String = "",
    val translatedWord: String = "",
    val isFavourite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
