package com.example.translator.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.translator.PairLanguageStore
import com.example.translator.data.datastore.serializer.PairLanguageItemStoreSerializer

val Context.pairLanguageItemStore: DataStore<PairLanguageStore> by dataStore(
    fileName = "pair_language_item_store.pb",
    serializer = PairLanguageItemStoreSerializer
)