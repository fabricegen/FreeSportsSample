package com.sports.freesportssample.ui.league

import androidx.lifecycle.SavedStateHandle
import com.sports.freesportssample.data.common.CoroutineDispatchers
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.test.CoroutineDispatcherRule
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.usecase.FetchLeaguesUseCase
import com.sports.freesportssample.domain.usecase.GetLeaguesByNameUseCase
import com.sports.freesportssample.domain.usecase.GetTeamsByLeagueUseCase
import com.sports.freesportssample.ui.league.list.LeagueListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeagueListViewModelTest {
    @get:Rule
    val dispatchersRule = CoroutineDispatcherRule()

    @MockK
    private lateinit var getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase

    @MockK
    private lateinit var getLeaguesByNameUseCase: GetLeaguesByNameUseCase

    @MockK(relaxed = true)
    private lateinit var fetchLeaguesUseCase: FetchLeaguesUseCase

    @MockK
    private lateinit var coroutineDispatchers: CoroutineDispatchers

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coEvery { coroutineDispatchers.default }.returns(dispatchersRule.testDispatcher)
        coEvery { getLeaguesByNameUseCase(any()) }.returns(mockLeagues)
    }

    @Test
    fun `given leagues when init then return leagues`() {
        val mockSavedStateFlow = MutableStateFlow<String?>(null)
        mockSavedStateFlow.value = "French Ligue 1"
        coEvery { savedStateHandle.getStateFlow<String?>(any(), null) }.returns(mockSavedStateFlow)

        val leagueListViewModel = LeagueListViewModel(
            savedStateHandle,
            fetchLeaguesUseCase,
            getLeaguesByNameUseCase,
            getTeamsByLeagueUseCase,
            coroutineDispatchers
        )

        runTest(dispatchersRule.testDispatcher) {
            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())

            coVerify { fetchLeaguesUseCase() }
        }
    }

    @Test
    fun `given leagues when search teams then return filtered data`() {
        coEvery { getTeamsByLeagueUseCase(any()) }.returns(mockTeams)
        val mockSavedStateFlow = MutableStateFlow<String?>(null)
        mockSavedStateFlow.value = "French Ligue 1"
        coEvery { savedStateHandle.getStateFlow<String?>(any(), null) }.returns(mockSavedStateFlow)

        val leagueListViewModel = LeagueListViewModel(
            savedStateHandle,
            fetchLeaguesUseCase,
            getLeaguesByNameUseCase,
            getTeamsByLeagueUseCase,
            coroutineDispatchers
        )

        runTest(dispatchersRule.testDispatcher) {
            leagueListViewModel.searchTeams("French Ligue 1")

            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())
            Assert.assertTrue(leagueListViewModel.uiState.value.teams.isNotEmpty())

            coVerify { fetchLeaguesUseCase() }
            coVerify { getTeamsByLeagueUseCase(any()) }
        }
    }

    @Test
    fun `given leagues when search teams and error happened then return error message to user`() {
        coEvery { getTeamsByLeagueUseCase(any()) }.throws(RuntimeException("Error happened"))
        val mockSavedStateFlow = MutableStateFlow<String?>(null)
        mockSavedStateFlow.value = "French Ligue 1"
        coEvery { savedStateHandle.getStateFlow<String?>(any(), null) }.returns(mockSavedStateFlow)

        val leagueListViewModel = LeagueListViewModel(
            savedStateHandle,
            fetchLeaguesUseCase,
            getLeaguesByNameUseCase,
            getTeamsByLeagueUseCase,
            coroutineDispatchers
        )

        runTest(dispatchersRule.testDispatcher) {
            leagueListViewModel.searchTeams("French Ligue 1")

            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())
            Assert.assertNotNull(leagueListViewModel.uiState.value.errorMessage)

            coVerify { fetchLeaguesUseCase() }
            coVerify { getTeamsByLeagueUseCase(any()) }
        }
    }

    @Test
    fun `given search completed when clear search then clear teams and error`() {
        coEvery { getTeamsByLeagueUseCase(any()) }.returns(mockTeams)
        val mockSavedStateFlow = MutableStateFlow<String?>(null)
        mockSavedStateFlow.value = "French Ligue 1"
        coEvery { savedStateHandle.getStateFlow<String?>(any(), null) }.returns(mockSavedStateFlow)

        val leagueListViewModel = LeagueListViewModel(
            savedStateHandle,
            fetchLeaguesUseCase,
            getLeaguesByNameUseCase,
            getTeamsByLeagueUseCase,
            coroutineDispatchers
        )

        runTest(dispatchersRule.testDispatcher) {
            leagueListViewModel.searchTeams("French Ligue 1")

            Assert.assertTrue(leagueListViewModel.uiState.value.teams.isNotEmpty())

            leagueListViewModel.clearTeams()
            Assert.assertTrue(leagueListViewModel.uiState.value.teams.isEmpty())
            Assert.assertNull(leagueListViewModel.uiState.value.errorMessage)
        }
    }

    private val mockLeagues = listOf(
        League(id = 0, name = "Montpellier"),
        League(id = 1, name = "AAW Pro Mens"),
        League(id = 2, name = "Nice"),
        League(id = 3, name = "Monaco")
    )

    private val mockTeams = listOf(
        Team(id = 0, name = "Montpellier", badge = ""),
        Team(id = 1, name = "AAW Pro Mens", badge = ""),
        Team(id = 2, name = "Nice", badge = ""),
        Team(id = 3, name = "Monaco", badge = "")
    )
}