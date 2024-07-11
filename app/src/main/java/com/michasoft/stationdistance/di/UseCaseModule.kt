package com.michasoft.stationdistance.di

import com.michasoft.stationdistance.usecase.NormalizeStationKeywordsUseCase
import com.michasoft.stationdistance.usecase.NormalizeStationKeywordsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseModule {
    @Singleton
    @Binds
    fun bindNormalizeStationKeywordsUseCase(impl: NormalizeStationKeywordsUseCaseImpl): NormalizeStationKeywordsUseCase
}