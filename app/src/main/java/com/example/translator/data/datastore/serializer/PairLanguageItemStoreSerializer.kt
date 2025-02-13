package com.example.translator.data.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.translator.PairLanguageStore
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object PairLanguageItemStoreSerializer: Serializer<PairLanguageStore> {
    override val defaultValue: PairLanguageStore
        get() = PairLanguageStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PairLanguageStore {
        return try {
            PairLanguageStore.parseFrom(input)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PairLanguageStore, output: OutputStream) {
        t.writeTo(output)
    }

}