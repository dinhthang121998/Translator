package com.example.domain

import com.example.datastore.ProtoPreferenceManager
import com.example.model.SearchLanguageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StorePairLanguageUseCase
    @Inject
    constructor(private val protoPreferenceManager: ProtoPreferenceManager) :
    BaseUseCase<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>, Unit> {
        override fun invoke(param: Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>): Flow<Unit> =
            flow {
                protoPreferenceManager.savePairLanguageItem(param)
            }
    }
