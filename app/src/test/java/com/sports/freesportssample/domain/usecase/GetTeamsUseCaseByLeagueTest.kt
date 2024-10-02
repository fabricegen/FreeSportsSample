package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.test.CoroutineDispatcherRule
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.repository.LeaguesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetTeamsUseCaseByLeagueTest {
    @get:Rule
    val dispatchersRule = CoroutineDispatcherRule()

    @MockK
    private lateinit var leaguesRepository: LeaguesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given league when has teams return filtered data`() {
        coEvery { leaguesRepository.getTeams(leagueName = any()) }.returns(mockTeams)
        val getTeamsUseCaseByLeague = GetTeamsUseCaseByLeague(leaguesRepository)

        runTest(dispatchersRule.testDispatcher) {
            val result = getTeamsUseCaseByLeague("French Ligue 1")

            Assert.assertTrue(result!!.size == 2)
            Assert.assertTrue(result[0].id == 2)
            Assert.assertTrue(result[1].id == 0)

            coVerify { leaguesRepository.getTeams(any()) }
        }
    }

    private val mockTeams = listOf(
        Team(id = 0, name = "Montpellier", badge = ""),
        Team(id = 1, name = "AAW Pro Mens", badge = ""),
        Team(id = 2, name = "Nice", badge = ""),
        Team(id = 3, name = "Monaco", badge = "")
    )
}