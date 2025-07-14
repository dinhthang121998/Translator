package com.example.domain

import com.example.domain.base.SuspendUseCase
import com.example.mlkit.utils.TranslationUtils
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TranslateUseCase
    @Inject
    constructor(
        private val translationUtils: TranslationUtils,
        coroutineDispatcher: CoroutineDispatcher,
    ) :
    SuspendUseCase<TranslateUseCase.TranslationInput, String>(coroutineDispatcher) {
        override suspend fun execute(parameter: TranslationInput): String {
            return translationUtils.translate(
                parameter.originalText,
                parameter.fromLanguageCode,
                parameter.toLanguageCode,
            )
        }

        data class TranslationInput(
            val originalText: String,
            val fromLanguageCode: String,
            val toLanguageCode: String,
        )
    }
