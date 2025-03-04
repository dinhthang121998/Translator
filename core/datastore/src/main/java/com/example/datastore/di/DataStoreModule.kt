package com.example.datastore.di

import android.content.Context
import com.example.cipher.CryptoManager
import com.example.datastore.DatastorePrefManager
import com.example.datastore.DatastoreProtoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    fun provideDatastoreProtoManager(
        @ApplicationContext context: Context,
    ): DatastoreProtoManager {
        return DatastoreProtoManager(context)
    }

    @Provides
    @Singleton
    fun provideDataStorePrefManager(
        @ApplicationContext context: Context,
        cryptoManager: CryptoManager,
    ): DatastorePrefManager {
        return DatastorePrefManager(context, cryptoManager)
    }
}
