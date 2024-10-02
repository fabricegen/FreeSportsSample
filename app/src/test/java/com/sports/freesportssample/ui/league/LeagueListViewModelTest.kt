package com.sports.freesportssample.ui.league

import com.sports.freesportssample.data.common.CoroutineDispatchers
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.test.CoroutineDispatcherRule
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.repository.LeaguesRepository
import com.sports.freesportssample.domain.usecase.GetLeaguesUseCase
import com.sports.freesportssample.domain.usecase.GetTeamsUseCaseByLeague
import com.sports.freesportssample.ui.league.list.LeagueListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeagueListViewModelTest {
    @get:Rule
    val dispatchersRule = CoroutineDispatcherRule()

    @MockK
    private lateinit var getTeamsUseCaseByLeague: GetTeamsUseCaseByLeague

    @MockK
    private lateinit var getLeaguesUseCase: GetLeaguesUseCase

    @MockK
    private lateinit var coroutineDispatchers: CoroutineDispatchers

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given leagues when init then return leagues`() {
        coEvery { getLeaguesUseCase() }.returns(mockLeagues)
        coEvery { coroutineDispatchers.default }.returns(dispatchersRule.testDispatcher)

        val leagueListViewModel = LeagueListViewModel(getLeaguesUseCase, getTeamsUseCaseByLeague, coroutineDispatchers)

        runTest(dispatchersRule.testDispatcher) {
            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())

            coVerify { getLeaguesUseCase() }
        }
    }

    @Test
    fun `given leagues when search teams then return filtered data`() {
        coEvery { getLeaguesUseCase() }.returns(mockLeagues)
        coEvery { getTeamsUseCaseByLeague(any()) }.returns(mockTeams)
        coEvery { coroutineDispatchers.default }.returns(dispatchersRule.testDispatcher)

        val leagueListViewModel = LeagueListViewModel(getLeaguesUseCase, getTeamsUseCaseByLeague, coroutineDispatchers)

        runTest(dispatchersRule.testDispatcher) {
            leagueListViewModel.searchTeams("French Ligue 1")

            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())
            Assert.assertTrue(leagueListViewModel.uiState.value.teams.isNotEmpty())

            coVerify { getLeaguesUseCase() }
            coVerify { getTeamsUseCaseByLeague(any()) }
        }
    }

    @Test
    fun `given leagues when search teams and error happened then return error message to user`() {
        coEvery { getLeaguesUseCase() }.returns(mockLeagues)
        coEvery { getTeamsUseCaseByLeague(any()) }.throws(RuntimeException("Error happened"))
        coEvery { coroutineDispatchers.default }.returns(dispatchersRule.testDispatcher)

        val leagueListViewModel = LeagueListViewModel(getLeaguesUseCase, getTeamsUseCaseByLeague, coroutineDispatchers)

        runTest(dispatchersRule.testDispatcher) {
            leagueListViewModel.searchTeams("French Ligue 1")

            Assert.assertTrue(leagueListViewModel.uiState.value.leagues.isNotEmpty())
            Assert.assertNotNull(leagueListViewModel.uiState.value.errorMessage)

            coVerify { getLeaguesUseCase() }
            coVerify { getTeamsUseCaseByLeague(any()) }
        }
    }

    @Test
    fun `given search completed when clear search then clear teams and error`() {
        coEvery { getLeaguesUseCase() }.returns(mockLeagues)
        coEvery { getTeamsUseCaseByLeague(any()) }.returns(mockTeams)
        coEvery { coroutineDispatchers.default }.returns(dispatchersRule.testDispatcher)

        val leagueListViewModel = LeagueListViewModel(getLeaguesUseCase, getTeamsUseCaseByLeague, coroutineDispatchers)

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