package com.michasoft.stationdistance.network.dto

import com.google.gson.annotations.SerializedName
import com.michasoft.stationdistance.model.StationKeyword

data class StationKeywordDto(
    val id: Int,
    val keyword: String,
    @SerializedName("station_id") val stationId: Int
) {
    fun toModel() = StationKeyword(
        id = id,
        keyword = keyword,
        stationId = stationId
    )
}