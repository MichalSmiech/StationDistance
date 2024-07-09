package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.datasource.StationRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModule {
    @Binds
    fun bindStationRemoteDataSource(impl: StationRemoteDataSourceImpl): StationRemoteDataSource
}