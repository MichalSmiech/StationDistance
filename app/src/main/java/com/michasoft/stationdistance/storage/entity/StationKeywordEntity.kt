package com.michasoft.stationdistance.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michasoft.stationdistance.model.StationKeyword

@Entity(tableName = StationKeywordEntity.TABLE_NAME)
class StationKeywordEntity(
    @PrimaryKey
    val id: Int,
    val keyword: String,
    val stationId: Int
) {
    constructor(station: StationKeyword) : this(
        station.id,
        station.keyword,
        station.stationId,
    )

    companion object {
        const val TABLE_NAME = "StationKeywords"
    }
}