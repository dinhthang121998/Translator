package com.example.domain.di

import com.example.data.repository.TranslationHistoryRepository
import com.example.data.repository.WordInformationRepository
import com.example.datastore.DatastoreProtoManager
import com.example.domain.AddTranslationHistoryUseCase
import com.example.domain.DeleteTranslationHistoryUseCase
import com.example.domain.GetFavoriteTranslationHistoryUseCase
import com.example.domain.GetPairLanguageUseCase
import com.example.domain.GetTranslationHistoryUseCase
import com.example.domain.GetWordInformationUseCase
import com.example.domain.StorePairLanguageUseCase
import com.example.domain.UndoTranslationHistoryUseCase
import com.example.domain.UpdateFavoriteTranslationHistoryUseCase
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
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): AddTranslationHistoryUseCase {
        return AddTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetWordInformationUseCase(
        wordInformationRepository: WordInformationRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetWordInformationUseCase {
        return GetWordInformationUseCase(wordInformationRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetTranslationHistoryUseCase(
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetTranslationHistoryUseCase {
        return GetTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideUpdateTranslatedFavoriteUseCase(
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UpdateFavoriteTranslationHistoryUseCase {
        return UpdateFavoriteTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideDeleteTranslatedUseCase(
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): DeleteTranslationHistoryUseCase {
        return DeleteTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteTranslationHistoryUseCase(
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): GetFavoriteTranslationHistoryUseCase {
        return GetFavoriteTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }

    @Provides
    @Singleton
    fun provideUndoTranslationHistoryUseCase(
        translationHistoryRepository: TranslationHistoryRepository,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UndoTranslationHistoryUseCase {
        return UndoTranslationHistoryUseCase(translationHistoryRepository, coroutineDispatcher)
    }
}
