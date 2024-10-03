package com.sports.freesportssample.ui.league.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sports.freesportssample.R
import com.sports.freesportssample.ui.common.UiErrorMessage
import com.sports.freesportssample.ui.league.list.components.SearchErrorView
import com.sports.freesportssample.ui.league.list.components.SearchLoaderView
import com.sports.freesportssample.ui.league.list.components.SearchSuggestionsView
import com.sports.freesportssample.ui.league.list.components.SearchTeamsContentView
import com.sports.freesportssample.ui.league.list.model.LeagueUi
import com.sports.freesportssample.ui.theme.FreeSportsSampleTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListScreen(
    modifier: Modifier = Modifier,
    uiState: LeagueListUiState,
    onLeagueSelected: (String) -> Unit,
    onSearchCleared: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val filteredItems = remember(searchText) {
        uiState.leagues.filter {
            it.name.startsWith(searchText, ignoreCase = true) && searchText.isNotEmpty()
        }.toPersistentList()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = if (expanded) {
                        MaterialTheme.colorScheme.background
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerLow
                    },
                ),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = { query ->
                            searchText = query
                        },
                        onSearch = { expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = { Text(stringResource(id = R.string.search_by_league)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    searchText = ""
                                    expanded = false
                                    onSearchCleared()
                                },
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                SearchSuggestionsView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    filteredItems = filteredItems,
                    onLeagueSelected = {
                        expanded = false
                        searchText = it
                        onLeagueSelected(it)
                    }
                )
            }
        },
        content = { paddingValues ->
            uiState.errorMessage?.apply {
                SearchErrorView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    message = message
                )
            } ?: run {
                if (uiState.loading) {
                    SearchLoaderView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                } else {
                    SearchTeamsContentView(
                        modifier = Modifier.padding(paddingValues),
                        teams = uiState.teams
                    )
                }
            }
        })
}

@Preview
@Composable
private fun LeagueListScreenWithDataPreview() {
    FreeSportsSampleTheme {
        Surface {
            LeagueListScreen(
                modifier = Modifier,
                uiState = LeagueListUiState(
                    loading = false,
                    leagues = persistentListOf(
                        LeagueUi(1, "Arsenal"),
                        LeagueUi(2, "PSG"),
                        LeagueUi(3, "Olympique de Marseille"),
                        LeagueUi(4, "Monaco")
                    )
                ),
                onLeagueSelected = {},
                onSearchCleared = {}
            )
        }
    }
}

@Preview
@Composable
private fun LeagueListScreenWithErrorPreview() {
    FreeSportsSampleTheme {
        Surface {
            LeagueListScreen(
                modifier = Modifier,
                uiState = LeagueListUiState(
                    loading = false,
                    errorMessage = UiErrorMessage(message = "Error happened!")
                ),
                onLeagueSelected = {},
                onSearchCleared = {}
            )
        }
    }
}

