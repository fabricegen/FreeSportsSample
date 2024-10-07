package com.sports.freesportssample.data.repository

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.repository.LeaguesRepository
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class DefaultLeaguesRepository @Inject constructor(
    private val remoteLeaguesDataSource: RemoteLeaguesDataSource,
) : LeaguesRepository {
    override var leagues = emptyList<League>()

    override suspend fun fetchLeagues() = coroutineScope {
        leagues = remoteLeaguesDataSource.getLeagues()
            .sortedBy { it.name }
    }

    override suspend fun getTeams(leagueName: String): List<Team>? = remoteLeaguesDataSource.getTeams(leagueName)
}