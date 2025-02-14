package com.example.translator.di

import android.content.Context
import com.example.translator.data.datastore.ProtoPreferenceManager
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
    fun provideProtoPreferenceManager(
        @ApplicationContext context: Context,
    ): ProtoPreferenceManager {
        return ProtoPreferenceManager(context)
    }
}
