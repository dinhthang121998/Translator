package com.example.translator.di

import com.example.translator.data.datastore.ProtoPreferenceManager
import com.example.translator.domain.repository.HomeRepository
import com.example.translator.domain.usecase.DataStoreUseCase
import com.example.translator.domain.usecase.HomeUseCase
import com.example.translator.domain.usecase.LanguageUseCase
import com.example.translator.domain.usecase.SpeakingUseCase
import com.example.translator.domain.usecase.TranslationUseCase
import com.example.translator.util.TextToSpeechUtils
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
    fun provideLanguageUseCase(): LanguageUseCase {
        return LanguageUseCase()
    }

    @Provides
    @Singleton
    fun provideTranslationUseCase(): TranslationUseCase {
        return TranslationUseCase()
    }

    @Provides
    @Singleton
    fun provideDataStoreUseCase(protoPreferenceManager: ProtoPreferenceManager): DataStoreUseCase {
        return DataStoreUseCase(protoPreferenceManager)
    }

    @Provides
    @Singleton
    fun provideSpeakingUseCase(textToSpeechUtils: TextToSpeechUtils): SpeakingUseCase {
        return SpeakingUseCase(textToSpeechUtils)
    }


}
