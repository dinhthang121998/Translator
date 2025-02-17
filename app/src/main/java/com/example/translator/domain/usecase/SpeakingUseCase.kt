package com.example.translator.domain.usecase

import com.example.translator.util.TextToSpeechUtils

class SpeakingUseCase(private val textToSpeechUtils: TextToSpeechUtils) {

    fun speak(text: String, languageCode: String) {
        textToSpeechUtils.speak(text, languageCode)
    }

}