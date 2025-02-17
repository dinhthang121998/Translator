package com.example.translator.di

import com.example.translator.data.database.dao.TranslatedDao
import com.example.translator.data.remote.api.ApiService
import com.example.translator.data.repository.HomeRepositoryImpl
import com.example.translator.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideHomeRepository(translatedDao: TranslatedDao, apiService: ApiService): HomeRepository {
        return HomeRepositoryImpl(translatedDao, apiService)
    }
}
