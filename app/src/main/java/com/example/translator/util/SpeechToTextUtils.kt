package com.example.translator.util

import android.content.Context
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// TODO: CustomView for SpeechRecognizer
@Singleton
class SpeechToTextUtils
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        init {
        }
    }
