package com.michasoft.stationdistance.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.michasoft.stationdistance.storage.entity.StationEntity
import com.michasoft.stationdistance.storage.entity.StationKeywordEntity

@Dao
interface StationDao {
    @Insert
    suspend fun insertStations(stationEntities: List<StationEntity>)

    @Insert
    suspend fun insertStationKeywords(stationKeywordEntities: List<StationKeywordEntity>)

    @Query(
        "SELECT station.* FROM ${StationEntity.TABLE_NAME} station " +
                "ORDER BY station.hits DESC"
    )
    suspend fun getStations(): List<StationEntity>

    @Query(
        "SELECT DISTINCT station.* FROM ${StationEntity.TABLE_NAME} station " +
                "JOIN ${StationKeywordEntity.TABLE_NAME} keyword ON station.id = keyword.stationId " +
                "WHERE keyword.keyword LIKE :query " +
                "ORDER BY station.hits DESC"
    )
    suspend fun getStations(query: String): List<StationEntity>

    @Query("DELETE FROM ${StationEntity.TABLE_NAME}")
    suspend fun clearStation()

    @Query("DELETE FROM ${StationKeywordEntity.TABLE_NAME}")
    suspend fun clearStationKeywords()

    @Query("SELECT * FROM ${StationEntity.TABLE_NAME} WHERE id = :stationId")
    suspend fun getStation(stationId: Int): StationEntity?
}