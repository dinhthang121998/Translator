package com.example.data.repository

import com.example.model.PronunciationSpeed
import kotlinx.coroutines.flow.Flow

interface PronunciationSpeedRepository {
    suspend fun savePronunciationSpeed(speed: String)

    fun getPronunciationSpeedFlow(): Flow<List<PronunciationSpeed>>
}
