package com.michasoft.stationdistance.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import com.michasoft.stationdistance.ui.SearchStationActivity.Companion.RESULT_STATION_ID

private class SearchStationContract : ActivityResultContract<Int, SearchStationContract.Result?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, SearchStationActivity::class.java)
            .putExtra(MODE, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.let {
            Result(
                mode = it.getIntExtra(MODE, -1),
                stationId = it.getIntExtra(RESULT_STATION_ID, -1)
            )
        }
    }

    data class Result(
        val mode: Int,
        val stationId: Int
    )

    companion object {
        const val MODE = "mode"
        const val MODE_START_STATION = 0
        const val MODE_END_STATION = 1
    }
}

interface SearchStationLauncher {
    fun pickStartStation()
    fun pickEndStation()
}

@Composable
fun rememberSearchStationLauncher(
    onStartStationPick: (Int) -> Unit,
    onEndStationPick: (Int) -> Unit
): SearchStationLauncher {
    val currentOnStartStationPick = rememberUpdatedState(onStartStationPick)
    val currentOnEndStationPick = rememberUpdatedState(onEndStationPick)
    val activityLauncher =
        rememberLauncherForActivityResult(SearchStationContract()) { result ->
            if (result == null) {
                return@rememberLauncherForActivityResult
            }
            when (result.mode) {
                SearchStationContract.MODE_START_STATION -> {
                    currentOnStartStationPick.value(result.stationId)
                }

                SearchStationContract.MODE_END_STATION -> {
                    currentOnEndStationPick.value(result.stationId)
                }
            }
        }

    return object : SearchStationLauncher {
        override fun pickStartStation() {
            activityLauncher.launch(SearchStationContract.MODE_START_STATION)
        }

        override fun pickEndStation() {
            activityLauncher.launch(SearchStationContract.MODE_END_STATION)
        }

    }
}