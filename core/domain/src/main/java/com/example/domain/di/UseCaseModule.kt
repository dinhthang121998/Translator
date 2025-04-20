package com.example.domain.di

import com.example.data.repository.HomeRepository
import com.example.datastore.DatastoreProtoManager
import com.example.domain.AddTranslatedWordUseCase
import com.example.domain.DeleteTranslatedWordUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslatedWordUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.UpdateTranslatedFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetPairLanguageUseCase(
        protoPreferenceManager: DatastoreProtoManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetPairLanguageUseCase {
        return GetPairLanguageUseCase(protoPreferenceManager, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideStorePairLanguageUseCase(
        protoPreferenceManager: DatastoreProtoManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): StorePairLanguageUseCase {
        return StorePairLanguageUseCase(protoPreferenceManager, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideAddTranslatedWordUseCase(
        homeRepository: HomeRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): AddTranslatedWordUseCase {
        return AddTranslatedWordUseCase(homeRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetWordInformationUseCase(
        homeRepository: HomeRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetWordInformationUseCase {
        return GetWordInformationUseCase(homeRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetTranslatedWordUseCase(
        homeRepository: HomeRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetTranslatedWordUseCase {
        return GetTranslatedWordUseCase(homeRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideUpdateTranslatedFavoriteUseCase(
        homeRepository: HomeRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UpdateTranslatedFavoriteUseCase {
        return UpdateTranslatedFavoriteUseCase(homeRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideDeleteTranslatedUseCase(
        homeRepository: HomeRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): DeleteTranslatedWordUseCase {
        return DeleteTranslatedWordUseCase(homeRepository, coroutineDispatcher)
    }
}
