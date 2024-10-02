package com.sports.freesportssample.ui.league.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sports.freesportssample.data.common.CoroutineDispatchers
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.usecase.GetLeaguesUseCase
import com.sports.freesportssample.domain.usecase.GetTeamsUseCaseByLeague
import com.sports.freesportssample.ui.common.UiErrorMessage
import com.sports.freesportssample.ui.league.list.model.LeagueUi
import com.sports.freesportssample.ui.league.list.model.TeamUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LeagueListViewModel @Inject constructor(
    private val getLeaguesUseCase: GetLeaguesUseCase,
    private val getTeamsUseCaseByLeague: GetTeamsUseCaseByLeague,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeagueListUiState())
    val uiState = _uiState as StateFlow<LeagueListUiState>

    init {
        viewModelScope.launch(context = dispatchers.default) {
            try {
                _uiState.value = _uiState.value.copy(
                    leagues = getLeaguesUseCase()
                        .toLeaguesUi()
                        .toImmutableList()
                )
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
                    teams = getTeamsUseCaseByLeague(leagueName)
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