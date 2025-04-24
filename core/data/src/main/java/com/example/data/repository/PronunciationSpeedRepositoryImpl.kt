package com.example.data.repository

import com.example.datastore.DatastorePrefManager
import com.example.model.PronunciationSpeed
import com.example.model.SpeedType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PronunciationSpeedRepositoryImpl(
    private val datastorePrefManager: DatastorePrefManager,
) : PronunciationSpeedRepository {
    override suspend fun savePronunciationSpeed(speed: String) {
        datastorePrefManager.savePronunciationSpeed(speed)
    }

    override fun getPronunciationSpeedFlow(): Flow<List<PronunciationSpeed>> {
        return datastorePrefManager.getPronunciationSpeedFlow().map { speedName ->
            val currentSpeedName = speedName ?: SpeedType.NORMAL.typeName

            SpeedType.entries.map {
                PronunciationSpeed(it, isSelected = it.typeName == currentSpeedName)
            }
        }
    }
}
