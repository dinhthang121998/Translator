package com.example.mlkit

import com.example.common.LanguageUtils
import com.example.model.Downloadable
import com.example.model.SearchLanguageItem
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TranslationUtils {
    // TODO: handle exceptions
    suspend fun getAllLanguageItems(): List<SearchLanguageItem.LanguageItem> =
        withContext(Dispatchers.IO) {
            val listAllLanguages = TranslateLanguage.getAllLanguages()

            val getDownloadedLanguage = getDownloadedLanguages()
            listAllLanguages.map { languageCode ->
                val downloadable =
                    if (getDownloadedLanguage.contains(
                            languageCode,
                        )
                    ) {
                        com.example.model.Downloadable.IS_DOWNLOADED
                    } else {
                        com.example.model.Downloadable.NEED_DOWNLOAD
                    }

                SearchLanguageItem.LanguageItem(
                    LanguageUtils.convertLanguageCodeToName(languageCode),
                    languageCode,
                    downloadable,
                )
            }
        }

    suspend fun getDownloadedLanguages(): Set<String> =
        suspendCoroutine { continuation ->
            val modelManager = RemoteModelManager.getInstance()
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { models ->
                    continuation.resume(models.map { it.language }.toSet())
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    suspend fun filterLanguageItems(
        textFilter: String,
        listAllSearchLanguageItem: List<com.example.model.SearchLanguageItem.LanguageItem>,
    ): List<com.example.model.SearchLanguageItem.LanguageItem> =
        withContext(Dispatchers.Default) {
            if (textFilter.isNotEmpty()) {
                listAllSearchLanguageItem.filter { languageItem ->
                    languageItem.languageName.startsWith(
                        textFilter,
                        true,
                    )
                }
            } else {
                listAllSearchLanguageItem
            }
        }

    suspend fun updateDownloadedLanguage(
        downloadedLanguageItem: SearchLanguageItem.LanguageItem,
        listAllLanguageItem: List<SearchLanguageItem.LanguageItem>,
    ): MutableList<SearchLanguageItem.LanguageItem> =
        withContext(Dispatchers.Default) {
            // return the same address, the stateFlow can not emit the identical value -> use toMutableList()
            val updateAllLanguageItem: MutableList<SearchLanguageItem.LanguageItem> =
                listAllLanguageItem.toMutableList()
            val index =
                updateAllLanguageItem.indexOfFirst { it.languageCode == downloadedLanguageItem.languageCode }
            if (index != -1) {
                updateAllLanguageItem[index] = downloadedLanguageItem
            }
            updateAllLanguageItem
        }

    suspend fun downloadLanguageModel(languageItem: SearchLanguageItem.LanguageItem): com.example.model.SearchLanguageItem.LanguageItem =
        withContext(Dispatchers.IO) {
            var updateLanguageItem = languageItem
            if (downloadModel(languageItem.languageCode)) {
                updateLanguageItem = updateLanguageItem.copy(downloadable = Downloadable.IS_DOWNLOADED)
            } else {
                // TODO
            }
            updateLanguageItem
        }

    private suspend fun downloadModel(languageCode: String) =
        suspendCoroutine { continuation ->
            val modelManager = RemoteModelManager.getInstance()
            val model = TranslateRemoteModel.Builder(languageCode).build()
            val conditions =
                DownloadConditions.Builder()
                    .requireWifi()
                    .build()
            modelManager.download(model, conditions)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }

    suspend fun translate(
        originalText: String,
        fromLanguageCode: String,
        toLanguageCode: String,
    ): String =
        withContext(Dispatchers.IO) {
            getTextTranslated(originalText, fromLanguageCode, toLanguageCode)
        }

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

    fun swapText(
        fromText: String,
        toText: String,
    ): Pair<String, String> = Pair(toText, fromText)
}
