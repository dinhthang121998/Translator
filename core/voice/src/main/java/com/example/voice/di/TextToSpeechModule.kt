package com.example.voice.di

import android.content.Context
import com.example.voice.TextToSpeechUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TextToSpeechModule {
    @Provides
    @Singleton
    fun provideTextToSpeechUtils(
        @ApplicationContext context: Context,
    ): TextToSpeechUtils {
        return TextToSpeechUtils(context)
    }
}
