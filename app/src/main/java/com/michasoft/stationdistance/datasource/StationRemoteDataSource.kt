package com.michasoft.stationdistance.datasource

import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.model.StationKeyword
import com.michasoft.stationdistance.network.NetworkError
import com.michasoft.stationdistance.network.StationService
import com.michasoft.stationdistance.util.Either
import java.io.IOException
import javax.inject.Inject

interface StationRemoteDataSource {
    suspend fun getStations(): Either<List<Station>, NetworkError>
    suspend fun getStationKeywords(): Either<List<StationKeyword>, NetworkError>
}

class StationRemoteDataSourceImpl @Inject constructor(
    private val stationService: StationService
) : StationRemoteDataSource {
    override suspend fun getStations(): Either<List<Station>, NetworkError> {
        return runCatching { stationService.getStations().map { it.toModel() } }
    }

    override suspend fun getStationKeywords(): Either<List<StationKeyword>, NetworkError> {
        return runCatching { stationService.getStationKeywords().map { it.toModel() } }
    }

    private suspend fun <T> runCatching(block: suspend () -> T): Either<T, NetworkError> {
        return try {
            val result = block()
            Either.Success(result)
        } catch (exc: IOException) {
            Either.Failure(NetworkError.NoNetwork)
        } catch (exc: Exception) {
            Either.Failure(NetworkError.Other)
        }
    }
}