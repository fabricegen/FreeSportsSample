package com.sports.freesportssample.data.repository

import com.sports.freesportssample.domain.repository.LeaguesRepository
import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import javax.inject.Inject

class DefaultLeaguesRepository @Inject constructor(
    private val remoteLeaguesDataSource: RemoteLeaguesDataSource
) : LeaguesRepository {

    override suspend fun getLeagues(): List<League> = remoteLeaguesDataSource.getLeagues()

    override suspend fun getTeams(leagueName: String): List<Team>? = remoteLeaguesDataSource.getTeams(leagueName)
}