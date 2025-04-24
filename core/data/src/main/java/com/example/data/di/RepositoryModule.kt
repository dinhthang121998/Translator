package com.example.data.di

import com.example.data.repository.PronunciationSpeedRepository
import com.example.data.repository.PronunciationSpeedRepositoryImpl
import com.example.data.repository.TranslationHistoryRepository
import com.example.data.repository.TranslationHistoryRepositoryImpl
import com.example.data.repository.WordInformationRepository
import com.example.data.repository.WordInformationRepositoryImpl
import com.example.database.dao.TranslatedDao
import com.example.datastore.DatastorePrefManager
import com.example.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideTranslationHistoryRepository(translatedDao: TranslatedDao): TranslationHistoryRepository {
        return TranslationHistoryRepositoryImpl(translatedDao)
    }

    @Provides
    @Singleton
    fun provideWordInformationRepository(apiService: ApiService): WordInformationRepository {
        return WordInformationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePronunciationSpeedRepository(datastorePrefManager: DatastorePrefManager): PronunciationSpeedRepository {
        return PronunciationSpeedRepositoryImpl(datastorePrefManager)
    }
}
