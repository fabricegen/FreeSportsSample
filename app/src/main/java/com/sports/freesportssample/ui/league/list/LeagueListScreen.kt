package com.sports.freesportssample.ui.league.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sports.freesportssample.R
import com.sports.freesportssample.ui.common.UiErrorMessage
import com.sports.freesportssample.ui.league.list.components.SearchErrorView
import com.sports.freesportssample.ui.league.list.components.SearchLoaderView
import com.sports.freesportssample.ui.league.list.components.SearchSuggestionsView
import com.sports.freesportssample.ui.league.list.components.SearchTeamsContentView
import com.sports.freesportssample.ui.league.list.model.LeagueUi
import com.sports.freesportssample.ui.theme.FreeSportsSampleTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueListScreen(
    modifier: Modifier = Modifier,
    uiState: LeagueListUiState,
    onLeagueSelected: (String) -> Unit,
    onSearchProcessed: (String) -> Unit,
    onSearchCleared: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

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
                            onSearchProcessed(query)
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
                    leagues = uiState.leagues,
                    onLeagueSelected = {
                        expanded = false
                        searchText = it
                        onLeagueSelected(it)
                    }
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                )
            ) {
                uiState.errorMessage?.apply {
                    SearchErrorView(
                        modifier = Modifier
                            .fillMaxSize(),
                        message = message
                    )
                } ?: run {
                    if (uiState.loading) {
                        SearchLoaderView(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        SearchTeamsContentView(
                            modifier = Modifier,
                            teams = uiState.teams
                        )
                    }
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
                onSearchCleared = {},
                onSearchProcessed = {}
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
                onSearchCleared = {},
                onSearchProcessed = {}
            )
        }
    }
}

