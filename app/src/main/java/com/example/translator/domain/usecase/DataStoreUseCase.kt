package com.example.translator.domain.usecase

import android.util.Log
import com.example.translator.common.UiState
import com.example.translator.data.datastore.ProtoPreferenceManager
import com.example.translator.data.mapper.toLanguageItem
import com.example.translator.domain.model.SearchLanguageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreUseCase
    @Inject
    constructor(private val protoPreferenceManager: ProtoPreferenceManager) {
        val pairLanguageItemFlow: Flow<UiState<Pair<SearchLanguageItem.LanguageItem?, SearchLanguageItem.LanguageItem?>>> =
            protoPreferenceManager.pairLanguageItemData.map { pairLanguageItemStore ->
                UiState.Success(
                    data =
                        Pair(
                            pairLanguageItemStore.fromLanguageItemStore.toLanguageItem(),
                            pairLanguageItemStore.toLanguageItemStore.toLanguageItem(),
                        ),
                )
            }
                .catch { exception -> Log.d("AAAA", "exception = ${exception.localizedMessage}") }

        suspend fun storeLanguageItem(
            isFromLanguageItem: Boolean,
            languageItem: SearchLanguageItem.LanguageItem,
        ) {
            protoPreferenceManager.saveLanguageItem(isFromLanguageItem, languageItem)
        }

        suspend fun storeSwapLanguageItem(
            fromLanguageItem: SearchLanguageItem.LanguageItem,
            toLanguageItem: SearchLanguageItem.LanguageItem,
        ) {
            val swapPairLanguageItem = swapLanguageItem(fromLanguageItem, toLanguageItem)
            protoPreferenceManager.saveSwapLanguageItem(swapPairLanguageItem)
        }

        private fun swapLanguageItem(
            fromLanguageCode: SearchLanguageItem.LanguageItem,
            toLanguageCode: SearchLanguageItem.LanguageItem,
        ): Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem> {
            return Pair(toLanguageCode, fromLanguageCode)
        }
    }
