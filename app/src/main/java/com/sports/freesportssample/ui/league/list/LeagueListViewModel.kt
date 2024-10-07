package com.sports.freesportssample.ui.league.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.freesportssample.data.common.CoroutineDispatchers
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.usecase.FetchLeaguesUseCase
import com.sports.freesportssample.domain.usecase.GetLeaguesByNameUseCase
import com.sports.freesportssample.domain.usecase.GetTeamsByLeagueUseCase
import com.sports.freesportssample.ui.common.UiErrorMessage
import com.sports.freesportssample.ui.league.list.model.LeagueUi
import com.sports.freesportssample.ui.league.list.model.TeamUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val SEARCH_QUERY = "SEARCH_QUERY"

@HiltViewModel
class LeagueListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchLeaguesUseCase: FetchLeaguesUseCase,
    private val getLeaguesByNameUseCase: GetLeaguesByNameUseCase,
    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeagueListUiState())
    val uiState = _uiState as StateFlow<LeagueListUiState>

    init {
        fetchLeagues()
        initSearchQueries()
    }

    private fun initSearchQueries() {
        viewModelScope.launch(context = dispatchers.default) {
            savedStateHandle.getStateFlow<String?>(SEARCH_QUERY, null)
                .filterNotNull()
                .collectLatest { searchText ->
                    val leagues = getLeaguesByNameUseCase(searchText)
                    _uiState.value = _uiState.value.copy(
                        leagues = leagues.toLeaguesUi().toPersistentList()
                    )
                }
        }
    }

    private fun fetchLeagues() {
        viewModelScope.launch(context = dispatchers.default) {
            try {
                fetchLeaguesUseCase()
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error happened")
                _uiState.value = _uiState.value.copy(
                    errorMessage = UiErrorMessage(message = e.javaClass.toString())
                )
            }
        }
    }

    fun searchTeams(leagueName: String) {
        viewModelScope.launch(context = dispatchers.default) {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                _uiState.value = _uiState.value.copy(
                    teams = getTeamsByLeagueUseCase(leagueName)
                        ?.toTeamsUi()
                        ?.toImmutableList() ?: persistentListOf(),
                    loading = false
                )
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error happened")
                _uiState.value = _uiState.value.copy(
                    errorMessage = UiErrorMessage(message = e.javaClass.toString()),
                    loading = false
                )
            }
        }
    }

    fun searchLeagues(searchText: String) {
        savedStateHandle[SEARCH_QUERY] = searchText
    }

    fun clearTeams() {
        viewModelScope.launch(context = dispatchers.default) {
            _uiState.value = _uiState.value.copy(
                teams = persistentListOf(),
                errorMessage = null,
                loading = false
            )
        }
    }

    private fun List<League>.toLeaguesUi() = map {
        LeagueUi(
            id = it.id,
            name = it.name
        )
    }

    private fun List<Team>.toTeamsUi() = map {
        TeamUi(
            id = it.id,
            badge = it.badge
        )
    }
}