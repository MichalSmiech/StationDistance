package com.michasoft.stationdistance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.michasoft.stationdistance.network.StationService
import com.michasoft.stationdistance.storage.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://koleo.pl/api/v2/main/"

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideStationService(): StationService {
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(StationService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return AppDatabase.build(applicationContext)
    }

    @Provides
    fun provideAppDataStore(@ApplicationContext applicationContext: Context): DataStore<Preferences> =
        applicationContext.dataStore
}

private val Context.dataStore by preferencesDataStore(name = "appDataStore")