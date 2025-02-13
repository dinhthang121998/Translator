package com.example.translator.data.datastore

import android.content.Context
import com.example.translator.PairLanguageStore
import com.example.translator.data.mapper.toLanguageItemStore
import com.example.translator.domain.model.SearchLanguageItem
import com.example.translator.util.pairLanguageItemStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProtoPreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun saveLanguageItem(
        isFromLanguageItem: Boolean,
        languageItem: SearchLanguageItem.LanguageItem
    ) {
        context.pairLanguageItemStore.updateData { pairLanguageItemStore ->
            val languageItemStore = languageItem.toLanguageItemStore()
            if (isFromLanguageItem) {
                pairLanguageItemStore.toBuilder().setFromLanguageItemStore(languageItemStore)
                    .build()
            } else {
                pairLanguageItemStore.toBuilder().setToLanguageItemStore(languageItemStore).build()
            }
        }
    }

    suspend fun saveSwapLanguageItem(pair: Pair<SearchLanguageItem.LanguageItem, SearchLanguageItem.LanguageItem>) {
        context.pairLanguageItemStore.updateData { pairLanguageItemStore ->
            pairLanguageItemStore.toBuilder()
                .setFromLanguageItemStore(pair.first.toLanguageItemStore())
                .setToLanguageItemStore(pair.second.toLanguageItemStore()).build()

        }
    }

    val pairLanguageItemData: Flow<PairLanguageStore> = context.pairLanguageItemStore.data
}