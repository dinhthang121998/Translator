package com.example.domain

import com.example.domain.base.UseCase
import com.example.voice.TextToSpeechUtils
import javax.inject.Inject

class SpeakUseCase
    @Inject
    constructor(private val textToSpeechUtils: TextToSpeechUtils) :
    UseCase<SpeakUseCase.SpeakInput, Unit>() {
        override fun execute(parameter: SpeakInput) {
            textToSpeechUtils.speak(parameter.text, parameter.languageCode)
        }

        data class SpeakInput(
            val text: String,
            val languageCode: String,
        )
    }
