package com.michasoft.stationdistance.network

import com.michasoft.stationdistance.network.dto.StationDto
import com.michasoft.stationdistance.network.dto.StationKeywordDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface StationService {
    @Headers("X-KOLEO-Version: 1")
    @GET("stations")
    suspend fun getStations(): List<StationDto>

    @Headers("X-KOLEO-Version: 1")
    @GET("station_keywords")
    suspend fun getStationKeywords(): List<StationKeywordDto>
}