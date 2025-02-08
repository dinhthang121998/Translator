package com.example.translator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.translator.data.database.dao.TranslatedDao
import com.example.translator.data.database.entity.TranslatedEntity

@Database(
    entities = [TranslatedEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract val translatedDao: TranslatedDao
}