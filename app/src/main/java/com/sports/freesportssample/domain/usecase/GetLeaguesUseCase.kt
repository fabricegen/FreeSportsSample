package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.repository.LeaguesRepository
import javax.inject.Inject

class GetLeaguesUseCase @Inject constructor(
    private val leaguesRepository: LeaguesRepository
) {
    suspend operator fun invoke(): List<League> = leaguesRepository.getLeagues()
        .sortedBy { it.name }
}