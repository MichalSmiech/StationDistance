package com.michasoft.stationdistance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michasoft.stationdistance.model.LatLng
import com.michasoft.stationdistance.model.Station
import com.michasoft.stationdistance.ui.theme.AppTheme
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
                    onQueryChange = viewModel::changeQuery,
                    searchedItems = state.searchedStations,
                    onSearchedStationClick = {
                        //TODO
                    },
                    onDismiss = {
                        //TODO
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchStationScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    searchedItems: List<Station>?,
    onSearchedStationClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text(text = "Search station") },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable { onDismiss() },
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            )
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
    }
}

@Composable
private fun NoResults() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "No results"
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
            onDismiss = {}
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
            onDismiss = {}
        )
    }
}