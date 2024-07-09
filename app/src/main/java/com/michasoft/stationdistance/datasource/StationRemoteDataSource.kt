package com.michasoft.stationdistance.datasource

import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.model.StationKeyword
import com.michasoft.stationdistance.network.StationService
import javax.inject.Inject

interface StationRemoteDataSource {
    suspend fun getStations(): List<Station>
    suspend fun getStationKeywords(): List<StationKeyword>
}

class StationRemoteDataSourceImpl @Inject constructor(
    private val stationService: StationService
): StationRemoteDataSource {
    override suspend fun getStations(): List<Station> {
        return stationService.getStations().map { it.toModel() }
    }

    override suspend fun getStationKeywords(): List<StationKeyword> {
        return stationService.getStationKeywords().map { it.toModel() }
    }
}