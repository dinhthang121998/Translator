package com.example.mlkit.di

import android.content.Context
import com.example.mlkit.ImageProcessor
import com.example.mlkit.TextRecognition
import com.example.mlkit.textRecognition.TextRecognitionProcessor
import com.example.mlkit.utils.TranslationUtils
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
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
    @IntoMap
    @TextRecognitionKey(TextRecognition.LATIN_RECOGNITION)
    fun provideTextRecognitionProcessorBase(): ImageProcessor {
        val options = TextRecognizerOptions.DEFAULT_OPTIONS
        return TextRecognitionProcessor(options)
    }
}
