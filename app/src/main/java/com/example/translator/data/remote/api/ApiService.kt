package com.example.translator.data.remote.api

import com.example.translator.data.remote.dto.WordInformationDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v2/entries/en/{word}") // Example endpoint
    suspend fun getWordInformation(
        @Path("word") word: String,
    ): List<WordInformationDto>
}
