package com.example.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechUtils
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) :
    TextToSpeech.OnInitListener {
        private var isInitialized = false
        private val textToSpeech: TextToSpeech = TextToSpeech(context, this)

        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
            } else {
                Log.d("AAAA", "TextToSpeech initializes failed")
            }
        }

        fun speak(
            text: String,
            languageCode: String,
        ) {
            if (!isInitialized) return

            val availableLocales = textToSpeech.availableLanguages
            availableLocales?.forEach { locale ->
                Log.d("AAAA", "Available locale: ${locale.displayName}")
            }

            val locale = Locale(languageCode)
            val result = textToSpeech.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("AAAA", "Language not supported: $languageCode")
            } else {
                Log.d("AAAA", "Language supported: $languageCode")
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        fun shutdown() {
            textToSpeech.shutdown()
        }
    }
