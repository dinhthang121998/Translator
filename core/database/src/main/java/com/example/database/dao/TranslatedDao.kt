package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslatedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslationHistory(translationHistoryEntity: TranslationHistoryEntity)

    @Query("UPDATE translationhistoryentity SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteTranslationHistory(id: Int)

    @Query("SELECT * FROM translationhistoryentity WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllTranslationHistory(): Flow<List<TranslationHistoryEntity>>

    @Query("UPDATE translationhistoryentity SET isFavourite = :isFavorite WHERE id = :id")
    suspend fun updateTranslationHistoryFavorite(
        id: Int,
        isFavorite: Boolean,
    )

    @Query("SELECT * FROM translationhistoryentity WHERE isFavourite = 1 AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun getFavoredTranslationHistory(): Flow<List<TranslationHistoryEntity>>

    @Query(
        "SELECT * FROM translationhistoryentity WHERE translatedWord = :translatedWord " +
            "AND originalWord = :originalWord AND isDeleted = :isDeleted ORDER BY updatedAt DESC",
    )
    fun findTranslationByOriginalAndTranslated(
        originalWord: String,
        translatedWord: String,
        isDeleted: Boolean,
    ): TranslationHistoryEntity?

    @Query("UPDATE translationhistoryentity SET isDeleted = 1 WHERE isDeleted = 0")
    suspend fun deleteAllTranslationHistory()
}
