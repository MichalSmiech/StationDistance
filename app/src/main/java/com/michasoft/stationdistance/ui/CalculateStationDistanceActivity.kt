package com.michasoft.stationdistance.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.michasoft.stationdistance.ui.SearchStationContract.Companion.MODE_END_STATION
import com.michasoft.stationdistance.ui.SearchStationContract.Companion.MODE_START_STATION
import com.michasoft.stationdistance.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculateStationDistanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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

                                }

                                MODE_END_STATION -> {

                                }
                            }
                        }
                    Column {
                        Button(onClick = { searchStationLauncher.launch(MODE_START_STATION) }) {
                            Text(text = "START")
                        }
                        Button(onClick = { searchStationLauncher.launch(MODE_END_STATION) }) {
                            Text(text = "END")
                        }
                    }
                }
            }
        }
    }
}