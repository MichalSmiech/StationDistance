package com.michasoft.stationdistance.viewdata

import com.michasoft.stationdistance.model.Station

data class CalculateStationDistanceViewState(
    val startStation: Station?,
    val endStation: Station?,
    val stationDistance: Float?,
)