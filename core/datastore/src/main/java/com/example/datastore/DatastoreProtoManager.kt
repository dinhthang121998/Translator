package com.example.datastore

import android.content.Context
import com.example.datastore_proto.LanguageItemStore
import com.example.datastore_proto.PairLanguageStore
import com.example.model.SearchLanguageItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatastoreProtoManager
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

        private fun SearchLanguageItem.LanguageItem.toLanguageItemStore() =
            LanguageItemStore.newBuilder().setLanguageName(this.languageName)
                .setLanguageCode(this.languageCode).setDownloaded(this.downloadable.id)
    }
