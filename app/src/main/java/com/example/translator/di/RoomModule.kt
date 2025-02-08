package com.example.translator.di

import android.content.Context
import androidx.room.Room
import com.example.translator.data.database.AppDatabase
import com.example.translator.data.database.dao.TranslatedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db").build()
    }

    @Provides
    fun provideTranslatedDao(appDatabase: AppDatabase): TranslatedDao{
        return appDatabase.translatedDao
    }

}