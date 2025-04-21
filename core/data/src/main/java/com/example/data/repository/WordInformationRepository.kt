package com.example.data.repository

import com.example.model.WordInformation

interface WordInformationRepository {
    suspend fun getWordInformation(word: String): List<WordInformation>
}
