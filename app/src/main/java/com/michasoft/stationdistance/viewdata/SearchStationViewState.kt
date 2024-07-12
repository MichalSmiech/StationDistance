package com.michasoft.stationdistance.viewdata

import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.network.NetworkError

data class SearchStationViewState(
    val query: String,
    val searchedStations: List<Station>?,
    val dataState: DataState
) {
    sealed interface DataState {
        data object Loading : DataState
        data object Loaded : DataState
        data class Error(val value: NetworkError) : DataState
    }
}