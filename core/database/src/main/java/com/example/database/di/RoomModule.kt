package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.AppDatabase
import com.example.database.BuildConfig
import com.example.database.dao.TranslatedDao
import com.example.database.util.SQLCipherUtils
import com.example.datastore.DatastorePrefManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideByteArray(datastorePreManager: DatastorePrefManagerImpl): ByteArray {
        val key =
            runBlocking {
                datastorePreManager.getDBKey().firstOrNull() ?: generateKey().also {
                    datastorePreManager.saveDBKey(it)
                }
            }
        return SQLiteDatabase.getBytes(key.toCharArray())
    }

    private fun generateKey(): String {
        return List(32) { ('a'..'z').random() }.joinToString("")
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        passphrase: ByteArray,
    ): AppDatabase {
        if (BuildConfig.DEBUG) {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database.db",
            ).build()
        }

        val state = SQLCipherUtils.getDatabaseState(context, "app_database.db")
        if (state == SQLCipherUtils.State.UNENCRYPTED) {
            SQLCipherUtils.encrypt(
                context,
                "app_database.db",
                passphrase,
            )
        }
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db")
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideTranslatedDao(appDatabase: AppDatabase): TranslatedDao {
        return appDatabase.translatedDao
    }
}
