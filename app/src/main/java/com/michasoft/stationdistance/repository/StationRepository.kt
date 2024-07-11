package com.michasoft.stationdistance.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.michasoft.stationdistance.datasource.StationLocalDataSource
import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.usecase.NormalizeStationKeywordsUseCase
import com.michasoft.stationdistance.util.normalize
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface StationRepository {
    suspend fun getStations(query: String): List<Station>
}

class StationRepositoryImpl @Inject constructor(
    private val stationRemoteDataSource: StationRemoteDataSource,
    private val stationLocalDataSource: StationLocalDataSource,
    private val dataSource: DataStore<Preferences>,
    private val normalizeStationKeywordsUseCase: NormalizeStationKeywordsUseCase
): StationRepository {
    override suspend fun getStations(query: String): List<Station> {
        if (!isCacheTimestampValid()) {
            clearStationData()
            cacheStationData()
        }
        return stationLocalDataSource.getStations(query.normalize())
    }

    private suspend fun cacheStationData() {
        val stations = stationRemoteDataSource.getStations()
        val stationKeywords = stationRemoteDataSource.getStationKeywords().let {
            normalizeStationKeywordsUseCase.normalizeStationKeywords(it)
        }
        stationLocalDataSource.insertStationsAndStationKeywords(stations, stationKeywords)
        saveCacheTimestamp()
    }

    private suspend fun saveCacheTimestamp() {
        dataSource.edit { it[cacheTimestampKey] = System.currentTimeMillis() }
    }

    private suspend fun clearStationData() {
        stationLocalDataSource.clear()
    }

    private suspend fun getCacheTimestamp(): Long? {
        return dataSource.data.map { it[cacheTimestampKey] }.firstOrNull()
    }

    private suspend fun isCacheTimestampValid(): Boolean {
        val cacheTimestamp = getCacheTimestamp()
        return cacheTimestamp != null && System.currentTimeMillis() - cacheTimestamp < CACHE_LIFETIME
    }

    companion object {
        private val cacheTimestampKey = longPreferencesKey("cacheInfo")
        private const val CACHE_LIFETIME = 24 * 60 * 60 * 1000 // 24h
    }
}