package com.michasoft.stationdistance.usecase

import com.michasoft.stationdistance.model.Station
import javax.inject.Inject

interface CalculateStationDistanceUseCase {
    /**
     * @return distance in kilometers
     */
    fun calculateStationDistance(station1: Station, station2: Station): Float
}

@Suppress("UnnecessaryVariable")
class CalculateStationDistanceUseCaseImpl @Inject constructor() : CalculateStationDistanceUseCase {
    override fun calculateStationDistance(station1: Station, station2: Station): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            station1.location.latitude,
            station1.location.longitude,
            station2.location.latitude,
            station2.location.longitude,
            results
        )
        val distanceInMeters = results[0]
        val distanceInKilometers = distanceInMeters / 1000f
        return distanceInKilometers
    }
}