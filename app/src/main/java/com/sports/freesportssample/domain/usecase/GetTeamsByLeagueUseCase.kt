package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.domain.model.Team
import com.sports.freesportssample.domain.repository.LeaguesRepository
import javax.inject.Inject

class GetTeamsByLeagueUseCase @Inject constructor(
    private val leaguesRepository: LeaguesRepository
) {
    suspend operator fun invoke(leagueName: String): List<Team>? =
        leaguesRepository.getTeams(leagueName = leagueName)
            ?.filterIndexed { index, _ -> index % 2 == 0 }
            ?.sortedByDescending { it.name }
}