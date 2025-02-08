package com.example.translator.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Insert
import com.example.translator.data.database.entity.TranslatedEntity

@Dao
interface TranslatedDao {

    @Insert
    suspend fun insertTranslatedWord(translatedEntity: TranslatedEntity)

    @Delete
    suspend fun deleteTranslatedWord(translatedEntity: TranslatedEntity)



}