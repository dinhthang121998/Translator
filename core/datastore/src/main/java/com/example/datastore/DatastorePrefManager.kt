package com.example.datastore

import kotlinx.coroutines.flow.Flow

interface DatastorePrefManager {
    suspend fun saveKey(key: String)

    val keyFlow: Flow<String?>

    suspend fun savePronunciationSpeed(speed: String)

    fun getPronunciationSpeedFlow(): Flow<String?>
}
