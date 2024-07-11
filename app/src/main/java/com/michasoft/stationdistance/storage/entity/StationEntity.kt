package com.michasoft.stationdistance.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.stationdistance.model.LatLng
import com.michasoft.stationdistance.model.Station

@Entity(tableName = StationEntity.TABLE_NAME)
class StationEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val hits: Int
) {
    constructor(station: Station) : this(
        station.id,
        station.name,
        station.location.latitude,
        station.location.longitude,
        station.hits
    )

    fun toModel() = Station(id, name, LatLng(latitude, longitude), hits)

    companion object {
        const val TABLE_NAME = "Stations"
    }
}
