package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.repository.StationRepository
import com.michasoft.stationdistance.repository.StationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindStationRepository(impl: StationRepositoryImpl): StationRepository
}