package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.datasource.StationLocalDataSource
import com.michasoft.stationdistance.datasource.StationLocalDataSourceImpl
import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.datasource.StationRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModule {
    @Singleton
    @Binds
    fun bindStationRemoteDataSource(impl: StationRemoteDataSourceImpl): StationRemoteDataSource

    @Singleton
    @Binds
    fun bindStationLocalDataSource(impl: StationLocalDataSourceImpl): StationLocalDataSource
}