package com.example.translator.data.datastore

import android.content.Context
import com.example.translator.PairLanguageStore
import com.example.translator.data.mapper.toLanguageItemStore
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.util.pairLanguageItemStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProtoPreferenceManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        suspend fun savePairLanguageItem(pair: Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>) {
            context.pairLanguageItemStore.updateData { pairLanguageItemStore ->
                pairLanguageItemStore.toBuilder()
                    .setFromLanguageItemStore(pair.first.toLanguageItemStore())
                    .setToLanguageItemStore(pair.second.toLanguageItemStore()).build()
            }
        }

        val pairLanguageItemData: Flow<PairLanguageStore> = context.pairLanguageItemStore.data
    }
