package com.example.translator.di

import com.example.translator.domain.repository.HomeRepository
import com.example.translator.domain.usecase.HomeUseCase
import com.example.translator.domain.usecase.SearchLanguageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideHomeUseCase(homeRepository: HomeRepository): HomeUseCase {
        return HomeUseCase(homeRepository)
    }

    @Provides
    @Singleton
    fun provideSearchLanguageUseCase(): SearchLanguageUseCase {
        return SearchLanguageUseCase()
    }
}
