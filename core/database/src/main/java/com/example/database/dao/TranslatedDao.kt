package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslationHistory(translationHistoryEntity: TranslationHistoryEntity)

    @Delete
    suspend fun deleteTranslationHistory(translationHistoryEntity: TranslationHistoryEntity)

    @Query("SELECT * FROM translationhistoryentity ORDER BY updatedAt DESC")
    fun getAllTranslationHistory(): Flow<List<TranslationHistoryEntity>>

    @Query("UPDATE translationhistoryentity SET isFavourite = :isFavorite WHERE id = :id")
    suspend fun updateTranslationHistoryFavorite(
        id: Int,
        isFavorite: Boolean,
    )
}
