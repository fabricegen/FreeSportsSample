package com.sports.freesportssample.domain.repository

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.model.Team

interface LeaguesRepository {
    suspend fun fetchLeagues()
    val leagues: List<League>
    suspend fun getTeams(leagueName: String): List<Team>?
}