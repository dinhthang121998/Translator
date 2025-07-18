package com.example.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
            val THEME_KEY = booleanPreferencesKey("theme_key")
        }

        override suspend fun saveDBKey(key: String) {
            val encryptedData = cryptoManager.encrypt(key.toByteArray())
            context.dataStore.edit { preferences ->
                preferences[DB_KEY] = encryptedData
            }
        }

        override fun getDBKey(): Flow<String?> {
            return context.dataStore.data.map { preferences ->
                preferences[DB_KEY]?.let { String(cryptoManager.decrypt(it)) }
            }
        }

        override suspend fun saveTheme(isDarkMode: Boolean) {
            context.dataStore.edit { preferences ->
                preferences[THEME_KEY] = isDarkMode
            }
        }

        override fun getTheme(): Flow<Boolean?> {
            return context.dataStore.data.map { preferences ->
                preferences[THEME_KEY]
            }
        }
    }
