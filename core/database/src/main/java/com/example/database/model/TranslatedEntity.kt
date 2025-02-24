package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TranslatedEntity(
    val originalWord: String = "",
    val translatedWord: String = "",
    val isFavourite: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
