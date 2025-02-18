package com.example.translator.domain.usecase

import com.example.translator.common.UiState
import com.example.translator.data.datastore.ProtoPreferenceManager
import com.example.translator.data.mapper.toLanguageItem
import com.example.translator.domain.model.SearchLanguageItem
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
    }
