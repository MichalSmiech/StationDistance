package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.datasource.StationRemoteDataSourceImpl
import com.michasoft.stationdistance.repository.StationRepository
import com.michasoft.stationdistance.repository.StationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun bindStationRepository(impl: StationRepositoryImpl): StationRepository
}