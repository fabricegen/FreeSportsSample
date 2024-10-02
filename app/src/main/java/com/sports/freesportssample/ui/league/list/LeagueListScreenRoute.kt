package com.sports.freesportssample.ui.league.list

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LeagueListScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: LeagueListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LeagueListScreen(
        modifier = modifier,
        uiState = uiState,
        onLeagueSelected = {
            viewModel.searchTeams(it)
        },
        onSearchCleared = {
            viewModel.clearTeams()
        }
    )
}