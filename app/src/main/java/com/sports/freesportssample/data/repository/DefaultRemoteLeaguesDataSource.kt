package com.sports.freesportssample.data.repository

import com.sports.freesportssample.data.api.FreeSportsApiService
import com.sports.freesportssample.data.api.reponse.GetLeaguesResponse
import com.sports.freesportssample.data.api.reponse.GetTeamsResponse
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import javax.inject.Inject

class DefaultRemoteLeaguesDataSource @Inject constructor(
    private val apiService: FreeSportsApiService
) : RemoteLeaguesDataSource {

    override suspend fun getLeagues(): List<League> =
        apiService.getAllLeagues().toLeagues()

    override suspend fun getTeams(leagueName: String): List<Team>? =
        apiService.getAllTeamsForLeague(leagueName = leagueName).toTeams()

    private fun GetLeaguesResponse.toLeagues(): List<League> =
        leagues.map {
            League(
                id = it.idLeague,
                name = it.strLeague
            )
        }

    private fun GetTeamsResponse.toTeams(): List<Team>? =
        teams?.map {
            Team(
                id = it.idTeam,
                name = it.strTeam,
                badge = it.strBadge
            )
        }
}
