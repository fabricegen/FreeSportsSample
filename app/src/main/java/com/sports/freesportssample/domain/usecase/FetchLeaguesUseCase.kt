package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.domain.repository.LeaguesRepository
import javax.inject.Inject

class FetchLeaguesUseCase @Inject constructor(
    private val leaguesRepository: LeaguesRepository
) {
    suspend operator fun invoke() = leaguesRepository.fetchLeagues()

}