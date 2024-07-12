package com.michasoft.stationdistance.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michasoft.stationdistance.ui.SearchStationContract.Companion.MODE_END_STATION
import com.michasoft.stationdistance.ui.SearchStationContract.Companion.MODE_START_STATION
import com.michasoft.stationdistance.ui.theme.AppTheme
import com.michasoft.stationdistance.viewmodel.CalculateStationDistanceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculateStationDistanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<CalculateStationDistanceViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val searchStationLauncher =
                rememberLauncherForActivityResult(SearchStationContract()) { result ->
                    if (result == null) {
                        return@rememberLauncherForActivityResult
                    }
                    Log.d(
                        "asd",
                        "SearchStationContract mode=${result.mode}, station=${result.stationId}"
                    )
                    when (result.mode) {
                        MODE_START_STATION -> {
                            viewModel.setStartStation(result.stationId)
                        }

                        MODE_END_STATION -> {
                            viewModel.setEndStation(result.stationId)
                        }
                    }
                }
            AppTheme {
                CalculateStationDistanceScreen(
                    startStationName = state.startStation?.name,
                    endStationName = state.endStation?.name,
                    stationDistance = state.stationDistance,
                    onStartStationClick = { searchStationLauncher.launch(MODE_START_STATION) },
                    onEndStationClick = { searchStationLauncher.launch(MODE_END_STATION) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculateStationDistanceScreen(
    startStationName: String?,
    endStationName: String?,
    stationDistance: Float?,
    onStartStationClick: () -> Unit,
    onEndStationClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusable(false)
                        .clickable { onStartStationClick() },
                    value = startStationName ?: "",
                    onValueChange = {},
                    enabled = false,
                    label = { Text(text = "Start station") },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        //For Icons
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusable(false)
                        .clickable { onEndStationClick() },
                    value = endStationName ?: "",
                    onValueChange = {},
                    enabled = false,
                    label = { Text(text = "End station") },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        //For Icons
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (stationDistance != null) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = String.format("%.2f", stationDistance),
                        onValueChange = {},
                        readOnly = true,
                        suffix = { Text(text = "km") },
                        label = { Text(text = "Distance between stations") }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalculateStationDistanceScreenPreview() {
    AppTheme {
        CalculateStationDistanceScreen(
            null,
            null,
            null,
            onStartStationClick = {},
            onEndStationClick = {}
        )
    }
}