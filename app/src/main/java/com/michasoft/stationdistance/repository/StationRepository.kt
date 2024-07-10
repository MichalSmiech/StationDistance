package com.michasoft.stationdistance.repository

import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.model.LatLng
import com.michasoft.stationdistance.model.Station
import javax.inject.Inject

interface StationRepository {
    suspend fun getStations(query: String): List<Station>
}

class StationRepositoryImpl @Inject constructor(
    private val stationRemoteDataSource: StationRemoteDataSource
): StationRepository {
    override suspend fun getStations(query: String): List<Station> {
        return stationRemoteDataSource.getStations()
    }

}