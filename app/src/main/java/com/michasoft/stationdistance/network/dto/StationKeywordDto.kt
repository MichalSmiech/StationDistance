package com.michasoft.stationdistance.network.dto

import com.michasoft.stationdistance.model.StationKeyword

data class StationKeywordDto(
    val id: Int,
    val keyword: String,
    val stationId: Int
) {
    fun toModel() = StationKeyword(
        id = id,
        keyword = keyword,
        stationId = stationId
    )
}