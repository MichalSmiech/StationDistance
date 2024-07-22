package com.michasoft.stationdistance.network

import com.michasoft.stationdistance.network.dto.StationDto
import com.michasoft.stationdistance.network.dto.StationKeywordDto
import retrofit2.http.GET
import retrofit2.http.Headers

private const val xKoleoVersionHeader = "X-KOLEO-Version: 1"

interface StationService {
    @Headers(xKoleoVersionHeader)
    @GET("stations")
    suspend fun getStations(): List<StationDto>

    @Headers(xKoleoVersionHeader)
    @GET("station_keywords")
    suspend fun getStationKeywords(): List<StationKeywordDto>
}