package com.michasoft.stationdistance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michasoft.stationdistance.repository.StationRepository
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
    private val stateRepository: StationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        SearchStationViewState(
            query = "",
            searchedStations = null,
            dataState = DataState.LOADING
        )
    )
    val state: StateFlow<SearchStationViewState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun changeQuery(query: String) {
        _state.update {
            it.copy(
                query = query,
                dataState = DataState.LOADING
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
                val stations = stateRepository.getStations(query)
                _state.update {
                    it.copy(
                        searchedStations = stations,
                        dataState = DataState.LOADED
                    )
                }
            }.onFailure {
                if (it !is CancellationException) {
                    _state.update { it.copy(dataState = DataState.ERROR) }
                }
            }
        }
    }

    fun retry() {
        _state.update {
            it.copy(
                dataState = DataState.LOADING
            )
        }
        viewModelScope.launch {
            searchStations(state.value.query)
        }
    }
}