package com.michasoft.stationdistance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michasoft.stationdistance.network.NetworkError
import com.michasoft.stationdistance.repository.StationRepository
import com.michasoft.stationdistance.util.getOrElse
import com.michasoft.stationdistance.viewdata.SearchStationViewState
import com.michasoft.stationdistance.viewdata.SearchStationViewState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchStationViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        SearchStationViewState(
            query = "",
            searchedStations = null,
            dataState = DataState.Loading
        )
    )
    val state: StateFlow<SearchStationViewState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            searchStations(state.value.query)
        }
    }

    fun changeQuery(query: String) {
        _state.update {
            it.copy(
                query = query,
                dataState = DataState.Loading
            )
        }
        viewModelScope.launch {
            searchStations(query)
        }
    }

    private suspend fun searchStations(query: String) {
        searchJob?.cancelAndJoin()
        searchJob = viewModelScope.launch {
            runCatching {
                val stations = stationRepository.getStations(query).getOrElse { networkError ->
                    _state.update { it.copy(dataState = DataState.Error(networkError)) }
                    return@runCatching
                }
                _state.update {
                    it.copy(
                        searchedStations = stations,
                        dataState = DataState.Loaded
                    )
                }
            }.onFailure {
                if (it !is CancellationException) {
                    _state.update { it.copy(dataState = DataState.Error(NetworkError.Other)) }
                }
            }
        }
    }

    fun retry() {
        _state.update {
            it.copy(
                dataState = DataState.Loading
            )
        }
        viewModelScope.launch {
            searchStations(state.value.query)
        }
    }
}