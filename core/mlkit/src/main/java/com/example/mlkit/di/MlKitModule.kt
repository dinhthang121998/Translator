package com.example.mlkit.di

import android.content.Context
import com.example.mlkit.TextRecognitionUtils
import com.example.mlkit.TranslationUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MlKitModule {
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
