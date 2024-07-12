package com.michasoft.stationdistance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michasoft.stationdistance.repository.StationRepository
import com.michasoft.stationdistance.usecase.CalculateStationDistanceUseCase
import com.michasoft.stationdistance.util.getOrElse
import com.michasoft.stationdistance.viewdata.CalculateStationDistanceViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculateStationDistanceViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val calculateStationDistanceUseCase: CalculateStationDistanceUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        CalculateStationDistanceViewState(
            startStation = null,
            endStation = null,
            stationDistance = null
        )
    )
    val state: StateFlow<CalculateStationDistanceViewState> = _state.asStateFlow()

    fun setStartStation(stationId: Int) {
        viewModelScope.launch {
            val station = stationRepository.getStation(stationId).getOrElse { return@launch }
            _state.update {
                it.copy(startStation = station)
                    .setStationDistance()
            }
        }
    }

    fun setEndStation(stationId: Int) {
        viewModelScope.launch {
            val station = stationRepository.getStation(stationId).getOrElse { return@launch }
            _state.update {
                it.copy(endStation = station)
                    .setStationDistance()
            }
        }
    }

    private fun CalculateStationDistanceViewState.setStationDistance(): CalculateStationDistanceViewState {
        val distance = if (startStation != null && endStation != null) {
            calculateStationDistanceUseCase.calculateStationDistance(startStation, endStation)
        } else {
            null
        }
        return copy(stationDistance = distance)
    }

    private fun refreshStationDistance() {
        val startStation = state.value.startStation
        val endStation = state.value.endStation
        val distance = if (startStation != null && endStation != null) {
            calculateStationDistanceUseCase.calculateStationDistance(startStation, endStation)
        } else {
            null
        }
        _state.update { it.copy(stationDistance = distance) }
    }
}