package com.michasoft.stationdistance.repository

import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import javax.inject.Inject

interface StationRepository {

}

class StationRepositoryImpl @Inject constructor(
    private val stationRemoteDataSource: StationRemoteDataSource
): StationRepository {

}