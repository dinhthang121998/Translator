package com.example.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cipher.CryptoManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DatastorePrefManagerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val cryptoManager: CryptoManager,
    ) : DatastorePrefManager {
        companion object {
            val DB_KEY = stringPreferencesKey("db_key")
            val PRONUNCIATION_SPEED_KEY = stringPreferencesKey("pronunciation_speed_key")
        }

        override suspend fun saveKey(key: String) {
            val encryptedData = cryptoManager.encrypt(key.toByteArray())
            context.dataStore.edit { preferences ->
                preferences[DB_KEY] = encryptedData
            }
        }

        override val keyFlow: Flow<String?> =
            context.dataStore.data.map { preferences ->
                preferences[DB_KEY]?.let { String(cryptoManager.decrypt(it)) }
            }

        override suspend fun savePronunciationSpeed(speed: String) {
            context.dataStore.edit { preferences ->
                preferences[PRONUNCIATION_SPEED_KEY] = speed
            }
        }

        override fun getPronunciationSpeedFlow(): Flow<String?> =
            context.dataStore.data.map { preferences ->
                preferences[PRONUNCIATION_SPEED_KEY]
            }
    }
