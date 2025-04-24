package com.example.domain.pairLanguage

import com.example.common.UiState
import com.example.datastore.DatastoreProtoManager
import com.example.datastore_proto.LanguageItemStore
import com.example.domain.base.FlowUseCase
import com.example.model.Downloadable
import com.example.model.SearchLanguageItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPairLanguageUseCase
    @Inject
    constructor(private val protoPreferenceManager: DatastoreProtoManager, coroutineDispatcher: CoroutineDispatcher) :
    FlowUseCase<Unit, Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>>(coroutineDispatcher) {
        override fun execute(parameter: Unit): Flow<UiState<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>>> {
            return protoPreferenceManager.pairLanguageItemData.map { pairLanguageItemStore ->
                val pairLanguage =
                    Pair(
                        pairLanguageItemStore.fromLanguageItemStore.toLanguageItem(),
                        pairLanguageItemStore.toLanguageItemStore.toLanguageItem(),
                    )
                UiState.Success(data = pairLanguage)
            }
        }

        private fun LanguageItemStore.toLanguageItem(): SearchLanguageItem.LanguageItem {
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
