package com.example.translator.di

import com.example.translator.data.datastore.ProtoPreferenceManager
import com.example.translator.domain.repository.HomeRepository
import com.example.translator.domain.usecase.AddTranslatedWordUseCase
import com.example.translator.domain.usecase.GetPairLanguageUseCase
import com.example.translator.domain.usecase.GetWordInformationUseCase
import com.example.translator.domain.usecase.StorePairLanguageUseCase
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
    fun provideGetPairLanguageUseCase(protoPreferenceManager: ProtoPreferenceManager): GetPairLanguageUseCase {
        return GetPairLanguageUseCase(protoPreferenceManager)
    }

    @Provides
    @Singleton
    fun provideStorePairLanguageUseCase(protoPreferenceManager: ProtoPreferenceManager): StorePairLanguageUseCase {
        return StorePairLanguageUseCase(protoPreferenceManager)
    }

    @Provides
    @Singleton
    fun provideAddTranslatedWordUseCase(homeRepository: HomeRepository): AddTranslatedWordUseCase {
        return AddTranslatedWordUseCase(homeRepository)
    }

    @Provides
    @Singleton
    fun provideGetWordInformationUseCase(homeRepository: HomeRepository): GetWordInformationUseCase {
        return GetWordInformationUseCase(homeRepository)
    }
}
