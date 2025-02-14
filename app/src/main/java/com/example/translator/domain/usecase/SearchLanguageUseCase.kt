package com.example.translator.domain.usecase

import LanguageUtils
import android.util.Log
import com.example.translator.common.UiState
import com.example.translator.domain.model.Downloadable
import com.example.translator.domain.model.SearchLanguageItem
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchLanguageUseCase {
    // we are working with an asynchronous API (getDownloadedModels()), so we use callbackFlow, trySend and awaitClose
    fun getSearchLanguageItems(): Flow<UiState<MutableList<SearchLanguageItem.LanguageItem>>> =
        callbackFlow {
            val listAllLanguages = TranslateLanguage.getAllLanguages()
            val modelManager = RemoteModelManager.getInstance()
            val listLanguageItem = mutableListOf<SearchLanguageItem.LanguageItem>()
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { models ->
                    val supportedLanguages = models.map { it.language }
                    listAllLanguages.forEach { languageCode ->
                        val downloadable =
                            if (supportedLanguages.contains(languageCode)) Downloadable.IS_DOWNLOADED else Downloadable.NEED_DOWNLOAD

                        listLanguageItem.add(
                            SearchLanguageItem.LanguageItem(
                                LanguageUtils.convertLanguageCodeToName(languageCode),
                                languageCode,
                                downloadable,
                            ),
                        )
                    }
                    trySend(UiState.Success(data = listLanguageItem))
                }.addOnFailureListener { exception ->
                    trySend(UiState.Error(message = exception.message.toString()))
                    Log.d("AAAA", "Error fetching downloaded models: ${exception.message}")
                }
            awaitClose()
        }.flowOn(Dispatchers.IO)

    fun filterLanguage(
        textFilter: String,
        listAllSearchLanguageItem: MutableList<SearchLanguageItem.LanguageItem>,
    ): Flow<MutableList<SearchLanguageItem.LanguageItem>> =
        flow {
            val filter =
                listAllSearchLanguageItem.filter { searchLanguageItem ->
                    searchLanguageItem.languageName.startsWith(
                        textFilter,
                        true,
                    )
                }.toMutableList()
            emit(filter)
        }
}
