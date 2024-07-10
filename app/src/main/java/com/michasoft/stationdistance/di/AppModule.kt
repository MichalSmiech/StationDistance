package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.network.StationService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
}