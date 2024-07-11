package com.michasoft.stationdistance.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michasoft.stationdistance.storage.dao.StationDao
import com.michasoft.stationdistance.storage.entity.StationEntity
import com.michasoft.stationdistance.storage.entity.StationKeywordEntity

@Database(
    entities = [
        StationEntity::class,
        StationKeywordEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val stationDao: StationDao

    companion object {
        fun build(context: Context): AppDatabase {
            val databaseName = "AppDatabase.db"
            return Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
                .build()
        }
    }
}