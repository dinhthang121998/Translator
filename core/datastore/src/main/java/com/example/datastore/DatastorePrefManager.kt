package com.example.datastore

import kotlinx.coroutines.flow.Flow

interface DatastorePrefManager {
    suspend fun saveDBKey(key: String)

    fun getDBKey(): Flow<String?>

    suspend fun saveTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean?>
}
