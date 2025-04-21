package com.example.data.repository

import com.example.model.WordInformation
import com.example.network.ApiService
import com.example.network.mapper.toDomain

class WordInformationRepositoryImpl(private val apiService: ApiService) : WordInformationRepository {
    override suspend fun getWordInformation(word: String): List<WordInformation> {
        return apiService.getWordInformation(word).map { it.toDomain() }
    }
}
