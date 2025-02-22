package com.example.translator.di

import android.content.Context
import com.example.translator.util.TextRecognitionUtils
import com.example.translator.util.TextToSpeechUtils
import com.example.translator.util.TranslationUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {
    @Provides
    @Singleton
    fun provideTextToSpeechUtils(
        @ApplicationContext context: Context,
    ): TextToSpeechUtils {
        return TextToSpeechUtils(context)
    }

    @Provides
    @Singleton
    fun provideTranslationUtils(
        @ApplicationContext context: Context,
    ): TranslationUtils {
        return TranslationUtils()
    }

    @Provides
    @Singleton
    fun provideTextRecognitionUtils(
        @ApplicationContext context: Context,
    ): TextRecognitionUtils {
        return TextRecognitionUtils(context)
    }
}
