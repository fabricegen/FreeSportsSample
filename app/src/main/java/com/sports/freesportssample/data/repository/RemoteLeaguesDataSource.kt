package com.sports.freesportssample.data.repository

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team

interface RemoteLeaguesDataSource {
    suspend fun getLeagues(): List<League>

    suspend fun getTeams(leagueName: String): List<Team>?
}
