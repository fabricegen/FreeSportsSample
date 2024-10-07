package com.sports.freesportssample.data.repository

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.test.CoroutineDispatcherRule
import com.sports.freesportssample.domain.model.Team
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DefaultLeaguesRepositoryTest {
    @get:Rule
    val dispatchersRule = CoroutineDispatcherRule()

    @MockK
    private lateinit var remoteLeaguesDataSource: RemoteLeaguesDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given leagues when success then return data`() {
        coEvery { remoteLeaguesDataSource.getLeagues() }.returns(mockLeagues)
        val leaguesRepository = DefaultLeaguesRepository(remoteLeaguesDataSource)

        runTest(dispatchersRule.testDispatcher) {
            leaguesRepository.fetchLeagues()

            Assert.assertTrue(leaguesRepository.leagues.size == 4)

            coVerify { remoteLeaguesDataSource.getLeagues() }
        }
    }

    @Test
    fun `given teams when success then return data`() {
        coEvery { remoteLeaguesDataSource.getTeams(any()) }.returns(mockTeams)
        val leaguesRepository = DefaultLeaguesRepository(remoteLeaguesDataSource)

        runTest(dispatchersRule.testDispatcher) {
            val result = leaguesRepository.getTeams("French ligue 1")

            Assert.assertTrue(result?.size == 4)

            coVerify { remoteLeaguesDataSource.getTeams(any()) }
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