package com.sports.freesportssample.domain.repository

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team

interface LeaguesRepository {
    suspend fun getLeagues(): List<League>
    suspend fun getTeams(leagueName: String): List<Team>?
}