package com.michasoft.stationdistance.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.michasoft.stationdistance.datasource.StationLocalDataSource
import com.michasoft.stationdistance.datasource.StationRemoteDataSource
import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.network.NetworkError
import com.michasoft.stationdistance.usecase.NormalizeStationKeywordsUseCase
import com.michasoft.stationdistance.util.Either
import com.michasoft.stationdistance.util.asFailure
import com.michasoft.stationdistance.util.asSuccess
import com.michasoft.stationdistance.util.getOrElse
import com.michasoft.stationdistance.util.normalize
import com.michasoft.stationdistance.util.onFailure
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface StationRepository {
    suspend fun getStations(query: String): Either<List<Station>, NetworkError>

    suspend fun getStation(stationId: Int): Either<Station?, NetworkError>
}

class StationRepositoryImpl @Inject constructor(
    private val stationRemoteDataSource: StationRemoteDataSource,
    private val stationLocalDataSource: StationLocalDataSource,
    private val dataSource: DataStore<Preferences>,
    private val normalizeStationKeywordsUseCase: NormalizeStationKeywordsUseCase
) : StationRepository {
    override suspend fun getStations(query: String): Either<List<Station>, NetworkError> {
        checkStationData().onFailure { return it.asFailure() }
        return stationLocalDataSource.getStations(query.normalize()).asSuccess()
    }

    override suspend fun getStation(stationId: Int): Either<Station?, NetworkError> {
        checkStationData().onFailure { return it.asFailure() }
        return stationLocalDataSource.getStation(stationId).asSuccess()
    }

    private suspend fun cacheStationData(): Either<Unit, NetworkError> {
        val stations = stationRemoteDataSource.getStations().getOrElse { return it.asFailure() }
        val stationKeywords =
            stationRemoteDataSource.getStationKeywords().getOrElse { return it.asFailure() }
                .let { normalizeStationKeywordsUseCase.normalizeStationKeywords(it) }
        stationLocalDataSource.insertStationsAndStationKeywords(stations, stationKeywords)
        saveCacheTimestamp(System.currentTimeMillis())
        return Unit.asSuccess()
    }

    private suspend fun checkStationData(): Either<Unit, NetworkError> {
        if (!isCacheTimestampValid()) {
            clearStationData()
            cacheStationData().onFailure { return it.asFailure() }
        }
        return Unit.asSuccess()
    }

    private suspend fun saveCacheTimestamp(millis: Long) {
        dataSource.edit { it[cacheTimestampKey] = millis }
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