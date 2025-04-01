package com.example.domain.di

import com.example.data.repository.HomeRepository
import com.example.domain.AddTranslatedWordUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslatedWordUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.UpdateTranslatedFavoriteUseCase
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
    fun provideGetPairLanguageUseCase(protoPreferenceManager: com.example.datastore.DatastoreProtoManager): GetPairLanguageUseCase {
        return GetPairLanguageUseCase(protoPreferenceManager)
    }

    @Provides
    @Singleton
    fun provideStorePairLanguageUseCase(protoPreferenceManager: com.example.datastore.DatastoreProtoManager): StorePairLanguageUseCase {
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

    @Provides
    @Singleton
    fun provideGetTranslatedWordUseCase(homeRepository: HomeRepository): GetTranslatedWordUseCase {
        return GetTranslatedWordUseCase(homeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTranslatedFavoriteUseCase(homeRepository: HomeRepository): UpdateTranslatedFavoriteUseCase {
        return UpdateTranslatedFavoriteUseCase(homeRepository)
    }
}
