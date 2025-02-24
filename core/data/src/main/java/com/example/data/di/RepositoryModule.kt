package com.example.data.di

import com.example.data.repository.HomeRepository
import com.example.database.dao.TranslatedDao
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
    fun provideHomeRepository(
        translatedDao: TranslatedDao,
        apiService: ApiService,
    ): HomeRepository {
        return com.example.data.repository.HomeRepositoryImpl(translatedDao, apiService)
    }
}
