package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.repository.LeaguesRepository
import com.sports.freesportssample.test.CoroutineDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetLeaguesByNameUseCaseTest {
    @get:Rule
    val dispatchersRule = CoroutineDispatcherRule()

    @MockK
    private lateinit var leaguesRepository: LeaguesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given string when search for leagues return found league`() {
        coEvery { leaguesRepository.leagues }.returns(mockLeagues)
        val getLeaguesByNameUseCase = GetLeaguesByNameUseCase(leaguesRepository)

        runTest(dispatchersRule.testDispatcher) {
            val result = getLeaguesByNameUseCase("AA")

            Assert.assertTrue(result.size == 1)
            Assert.assertTrue(result[0].id == 1)

            coVerify { leaguesRepository.leagues }
        }
    }

    private val mockLeagues = listOf(
        League(id = 0, name = "Montpellier"),
        League(id = 1, name = "AAW Pro Mens"),
        League(id = 2, name = "Nice"),
        League(id = 3, name = "Monaco")
    )
}