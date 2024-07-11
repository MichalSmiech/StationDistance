package com.michasoft.stationdistance.viewdata

import com.michasoft.stationdistance.model.Station

data class SearchStationViewState(
    val query: String,
    val searchedStations: List<Station>?,
    val dataState: DataState
) {
    enum class DataState {
        LOADING, LOADED, ERROR
    }
}