package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.database.model.TranslatedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedDao {
    @Insert
    suspend fun insertTranslatedWord(translatedEntity: TranslatedEntity)

    @Delete
    suspend fun deleteTranslatedWord(translatedEntity: TranslatedEntity)

    @Query("SELECT * FROM translatedEntity ORDER BY id DESC")
    fun getAllTranslatedWord(): Flow<List<TranslatedEntity>>

    @Query("UPDATE translatedentity SET isFavourite = :isFavorite WHERE id = :id")
    suspend fun updateTranslatedWordFavorite(
        id: Int,
        isFavorite: Boolean,
    )
}
