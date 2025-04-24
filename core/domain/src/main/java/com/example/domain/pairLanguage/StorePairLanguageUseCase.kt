package com.example.domain.pairLanguage

import com.example.datastore.DatastoreProtoManager
import com.example.domain.base.SuspendUseCase
import com.example.model.SearchLanguageItem
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StorePairLanguageUseCase
    @Inject
    constructor(private val protoPreferenceManager: DatastoreProtoManager, coroutineDispatcher: CoroutineDispatcher) :
    SuspendUseCase<Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>, Unit>(coroutineDispatcher) {
        override suspend fun execute(parameter: Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>) {
            return protoPreferenceManager.savePairLanguageItem(parameter)
        }
    }
