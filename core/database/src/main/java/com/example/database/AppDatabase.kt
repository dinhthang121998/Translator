package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.TranslatedDao
import com.example.database.model.TranslationHistoryEntity

@Database(
    entities = [TranslationHistoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val translatedDao: TranslatedDao
}
