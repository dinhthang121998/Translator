package com.example.translator.domain.usecase

import android.util.Log
import com.example.translator.common.UiState
import com.example.translator.domain.model.Downloadable
import com.example.translator.domain.model.SearchLanguageItem
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LanguageUseCase {
    fun getAllLanguageItems(): Flow<UiState<List<SearchLanguageItem.LanguageItem>>> =
        flow {
            val listAllLanguages = TranslateLanguage.getAllLanguages()
            val listLanguageItem = mutableListOf<SearchLanguageItem.LanguageItem>()

            val getDownloadedLanguage = getDownloadedLanguages()
            listAllLanguages.forEach { languageCode ->
                val downloadable =
                    if (getDownloadedLanguage.contains(languageCode)) Downloadable.IS_DOWNLOADED else Downloadable.NEED_DOWNLOAD
                listLanguageItem.add(
                    SearchLanguageItem.LanguageItem(
                        LanguageUtils.convertLanguageCodeToName(languageCode),
                        languageCode,
                        downloadable,
                    ),
                )
            }
            emit(UiState.Success(listLanguageItem.toList()))
        }.flowOn(Dispatchers.IO)

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

    fun filterLanguage(
        textFilter: String,
        listAllSearchLanguageItem: MutableList<SearchLanguageItem.LanguageItem>,
    ): Flow<MutableList<SearchLanguageItem.LanguageItem>> =
        flow {
            val filter =
                if (textFilter.isEmpty()) {
                    listAllSearchLanguageItem
                } else {
                    listAllSearchLanguageItem.filter { searchLanguageItem ->
                        searchLanguageItem.languageName.startsWith(
                            textFilter,
                            true,
                        )
                    }.toMutableList()
                }
            emit(filter)
        }.flowOn(Dispatchers.Default)

    fun updateDownloadedLanguage(
        downloadedLanguageItem: SearchLanguageItem.LanguageItem,
        listAllLanguageItem: MutableList<SearchLanguageItem.LanguageItem>,
    ) = flow {
        // return the same address, the stateFlow can not emit the identical value -> use toMutableList()
        val updateAllLanguageItem: MutableList<SearchLanguageItem.LanguageItem> =
            listAllLanguageItem.toMutableList()
        val index =
            updateAllLanguageItem.indexOfFirst { it.languageCode == downloadedLanguageItem.languageCode }
        if (index != -1) {
            updateAllLanguageItem[index] = downloadedLanguageItem
        }
        emit(updateAllLanguageItem)
    }.flowOn(Dispatchers.Default)

    fun downloadLanguageModel(languageItem: SearchLanguageItem.LanguageItem): Flow<UiState<SearchLanguageItem.LanguageItem>> =
        flow {
            if (downloadModel(languageItem.languageCode)) {
                val downloadedLanguageItem =
                    languageItem.copy(downloadable = Downloadable.IS_DOWNLOADED)
                emit(UiState.Success(downloadedLanguageItem))
            } else {
                TODO()
            }
        }.flowOn(Dispatchers.IO)

    private suspend fun downloadModel(languageCode: String) =
        suspendCoroutine<Boolean> { continuation ->
            val modelManager = RemoteModelManager.getInstance()
            val model = TranslateRemoteModel.Builder(languageCode).build()
            val conditions =
                DownloadConditions.Builder()
                    .requireWifi()
                    .build()
            Log.d("AAAA", "languageCode = $languageCode, model = $model")
            modelManager.download(model, conditions)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
}
