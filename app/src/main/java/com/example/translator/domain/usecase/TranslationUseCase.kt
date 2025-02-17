package com.example.translator.domain.usecase

import com.example.translator.common.UiState
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TranslationUseCase {
    fun translate(
        originalText: String,
        fromLanguageCode: String,
        toLanguageCode: String,
    ): Flow<UiState<String>> =
        flow {
            emit(UiState.Success(getTextTranslated(originalText, fromLanguageCode, toLanguageCode)))
        }.flowOn(Dispatchers.IO)

    private suspend fun getTextTranslated(
        originalText: String,
        fromLanguageCode: String,
        toLanguageCode: String,
    ) = suspendCoroutine<String> { continuation ->
        val options = initTranslatorOptions(fromLanguageCode, toLanguageCode)

        val translator = Translation.getClient(options)
        translator.translate(originalText).addOnSuccessListener { textTranslated ->
            continuation.resume(textTranslated)
        }.addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
    }

    private fun initTranslatorOptions(
        fromLanguageCode: String,
        toLanguageCode: String,
    ): TranslatorOptions {
        return TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
            .setTargetLanguage(toLanguageCode).build()
    }

    fun swapText(fromText: String, toText: String): Flow<Pair<String, String>> = flow {
        emit(Pair(toText, fromText))
    }.flowOn(Dispatchers.Default)
}
