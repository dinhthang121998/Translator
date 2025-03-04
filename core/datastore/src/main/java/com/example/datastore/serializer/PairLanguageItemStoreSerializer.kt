package com.example.datastore.serializer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.cipher.CryptoManager
import com.example.datastore_proto.PairLanguageStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object PairLanguageItemStoreSerializer : Serializer<PairLanguageStore> {
    private val cryptoManager: CryptoManager = CryptoManager()

    override val defaultValue: PairLanguageStore
        get() = PairLanguageStore.getDefaultInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun readFrom(input: InputStream): PairLanguageStore {
        return try {
            val encryptedData = input.readBytes()
            val decrypted = cryptoManager.decrypt(String(encryptedData))

            PairLanguageStore.parseFrom(decrypted)
        } catch (exception: IOException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: PairLanguageStore,
        output: OutputStream,
    ) {
        val encrypted = cryptoManager.encrypt(t.toByteArray()).toByteArray()
        withContext(Dispatchers.IO) {
            output.write(encrypted)
        }
    }
}
