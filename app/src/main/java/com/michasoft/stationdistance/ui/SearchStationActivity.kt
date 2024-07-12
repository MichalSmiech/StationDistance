package com.michasoft.stationdistance.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michasoft.stationdistance.R
import com.michasoft.stationdistance.model.LatLng
import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.network.NetworkError
import com.michasoft.stationdistance.ui.theme.AppTheme
import com.michasoft.stationdistance.viewdata.SearchStationViewState.DataState
import com.michasoft.stationdistance.viewmodel.SearchStationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel by viewModels<SearchStationViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            AppTheme {
                SearchStationScreen(
                    query = state.query,
                    dataState = state.dataState,
                    onQueryChange = viewModel::changeQuery,
                    searchedItems = state.searchedStations,
                    onSearchedStationClick = {
                        setResult(RESULT_OK, intent.putExtra(RESULT_STATION_ID, it))
                        finish()
                    },
                    onDismiss = {
                        finish()
                    },
                    onRetry = {
                        viewModel.retry()
                    }
                )
            }
        }
    }

    companion object {
        const val RESULT_STATION_ID = "stationId"
    }
}

@Composable
private fun SearchStationScreen(
    query: String,
    dataState: DataState,
    onQueryChange: (String) -> Unit,
    searchedItems: List<Station>?,
    onSearchedStationClick: (Int) -> Unit,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text(text = stringResource(R.string.search_station_search_field_placeholder)) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable { onDismiss() },
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            )
            when (dataState) {
                DataState.Loading -> Loading()
                DataState.Loaded -> {
                    if (searchedItems != null) {
                        if (searchedItems.isEmpty()) {
                            NoResults()
                        } else {
                            SearchedStationList(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                searchedItems = searchedItems,
                                onSearchedStationClick = onSearchedStationClick
                            )
                        }
                    }
                }

                is DataState.Error -> {
                    when (dataState.value) {
                        NetworkError.NoNetwork -> NoNetworkError(onRetry)
                        NetworkError.Other -> OtherError(onRetry)
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun NoResults() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.search_station_no_results)
        )
    }
}

@Composable
private fun SearchedStationList(
    modifier: Modifier,
    searchedItems: List<Station>,
    onSearchedStationClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(searchedItems) {
            SearchedStationItemView(item = it, onClick = onSearchedStationClick)
        }
    }
}

@Composable
private fun SearchedStationItemView(item: Station, onClick: (Int) -> Unit) {
    Box(modifier = Modifier.clickable { onClick(item.id) }) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = item.name
        )
    }
}

@Composable
private fun NoNetworkError(onRetryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff, contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.search_station_no_network_error)
            )
            TextButton(onClick = onRetryClick) {
                Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.action_refresh))
            }
        }
    }
}

@Composable
private fun OtherError(onRetryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline, contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.search_station_other_error)
            )
            TextButton(onClick = onRetryClick) {
                Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.action_refresh))
            }
        }
    }
}

@Preview
@Composable
private fun SearchStationScreenPreview() {
    AppTheme {
        SearchStationScreen(
            "abcd",
            onQueryChange = { _ -> },
            searchedItems = listOf(
                Station(1, "abc", LatLng(1.0, 1.0), 1),
                Station(1, "agadfa", LatLng(1.0, 1.0), 1),
                Station(1, "advasvdas", LatLng(1.0, 1.0), 1),
                Station(1, "dasvdsav", LatLng(1.0, 1.0), 1),
                Station(1, "dasvdsavda", LatLng(1.0, 1.0), 1),
            ),
            onSearchedStationClick = { _ -> },
            onDismiss = {},
            dataState = DataState.Loaded,
            onRetry = {}
        )
    }
}

@Preview
@Composable
private fun SearchStationScreenPreviewNoResults() {
    AppTheme {
        SearchStationScreen(
            "abcd",
            onQueryChange = { _ -> },
            searchedItems = listOf(),
            onSearchedStationClick = { _ -> },
            onDismiss = {},
            dataState = DataState.Loaded,
            onRetry = {}
        )
    }
}

@Preview
@Composable
private fun SearchStationScreenPreviewLoading() {
    AppTheme {
        SearchStationScreen(
            "abcd",
            onQueryChange = { _ -> },
            searchedItems = listOf(),
            onSearchedStationClick = { _ -> },
            onDismiss = {},
            dataState = DataState.Loading,
            onRetry = {}
        )
    }
}

@Preview
@Composable
private fun SearchStationScreenPreviewError() {
    AppTheme {
        SearchStationScreen(
            "abcd",
            onQueryChange = { _ -> },
            searchedItems = listOf(),
            onSearchedStationClick = { _ -> },
            onDismiss = {},
            dataState = DataState.Error(NetworkError.Other),
            onRetry = {}
        )
    }
}