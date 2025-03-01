package com.example.domain

import com.example.common.UiState
import com.example.datastore.ProtoPreferenceManager
import com.example.datastore_proto.LanguageItemStore
import com.example.model.Downloadable
import com.example.model.SearchLanguageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPairLanguageUseCase
    @Inject
    constructor(private val protoPreferenceManager: ProtoPreferenceManager) :
    BaseUseCase<Unit, UiState<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>>> {
        override fun invoke(param: Unit): Flow<UiState<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>>> =
            protoPreferenceManager.pairLanguageItemData.map { pairLanguageItemStore ->
                val pairLanguage =
                    Pair(
                        pairLanguageItemStore.fromLanguageItemStore.toLanguageItem(),
                        pairLanguageItemStore.toLanguageItemStore.toLanguageItem(),
                    )
                UiState.Success(data = pairLanguage)
            }.flowOn(Dispatchers.IO)

        fun LanguageItemStore.toLanguageItem(): SearchLanguageItem.LanguageItem {
            return Downloadable.fromId(this.downloaded)?.let {
                SearchLanguageItem.LanguageItem(
                    this.languageName,
                    this.languageCode,
                    it,
                )
            } ?: run {
                SearchLanguageItem.LanguageItem()
            }
        }
    }
