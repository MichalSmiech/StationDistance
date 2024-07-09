package com.michasoft.stationdistance.network.dto

import com.michasoft.stationdistance.model.LatLng
import com.michasoft.stationdistance.model.Station

data class StationDto(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val hits: Int,
) {
    fun toModel() = Station(
        id = id,
        name = name,
        location = LatLng(latitude = latitude, longitude = longitude),
        hits = hits
    )
}