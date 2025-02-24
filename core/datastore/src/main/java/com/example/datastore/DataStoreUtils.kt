package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.datastore.serializer.PairLanguageItemStoreSerializer
import com.example.datastore_proto.PairLanguageStore

val Context.pairLanguageItemStore: DataStore<PairLanguageStore> by dataStore(
    fileName = "pair_language_item_store.pb",
    serializer = PairLanguageItemStoreSerializer,
)
