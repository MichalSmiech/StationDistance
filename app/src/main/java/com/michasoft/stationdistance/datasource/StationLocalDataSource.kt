package com.michasoft.stationdistance.datasource

import androidx.room.withTransaction
import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.model.StationKeyword
import com.michasoft.stationdistance.storage.AppDatabase
import com.michasoft.stationdistance.storage.entity.StationEntity
import com.michasoft.stationdistance.storage.entity.StationKeywordEntity
import javax.inject.Inject

interface StationLocalDataSource {
    suspend fun insertStationsAndStationKeywords(
        stations: List<Station>,
        stationKeywords: List<StationKeyword>
    )

    suspend fun getStations(query: String): List<Station>

    suspend fun clear()
}

class StationLocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : StationLocalDataSource {
    override suspend fun insertStationsAndStationKeywords(
        stations: List<Station>,
        stationKeywords: List<StationKeyword>
    ) {
        appDatabase.withTransaction {
            appDatabase.stationDao.insertStations(stations.map { StationEntity(it) })
            appDatabase.stationDao.insertStationKeywords(stationKeywords.map {
                StationKeywordEntity(
                    it
                )
            })
        }
    }

    override suspend fun getStations(query: String): List<Station> {
        if (query.isEmpty()) {
            return appDatabase.stationDao.getStations().map { it.toModel() }
        }
        return appDatabase.stationDao.getStations("%$query%").map { it.toModel() }
    }

    override suspend fun clear() {
        appDatabase.withTransaction {
            appDatabase.stationDao.clearStation()
            appDatabase.stationDao.clearStationKeywords()
        }
    }

}