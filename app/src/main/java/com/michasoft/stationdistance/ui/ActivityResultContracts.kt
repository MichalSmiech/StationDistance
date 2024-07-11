package com.michasoft.stationdistance.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.michasoft.stationdistance.ui.SearchStationActivity.Companion.RESULT_STATION_ID

class SearchStationContract : ActivityResultContract<Int, SearchStationContract.Result?>() {
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