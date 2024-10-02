package com.sports.freesportssample.ui.league.list

import com.sports.freesportssample.ui.league.list.model.LeagueUi
import com.sports.freesportssample.ui.common.UiErrorMessage
import com.sports.freesportssample.ui.league.list.model.TeamUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LeagueListUiState(
    val loading: Boolean = false,
    val leagues: ImmutableList<LeagueUi> = persistentListOf(),
    val teams: ImmutableList<TeamUi> = persistentListOf(),
    val errorMessage: UiErrorMessage? = null
)