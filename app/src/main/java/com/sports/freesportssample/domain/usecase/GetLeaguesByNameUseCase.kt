package com.sports.freesportssample.domain.usecase

import com.sports.freesportssample.domain.model.League
import com.sports.freesportssample.domain.repository.LeaguesRepository
import timber.log.Timber
import javax.inject.Inject

class GetLeaguesByNameUseCase @Inject constructor(
    private val leaguesRepository: LeaguesRepository
) {
    operator fun invoke(searchText: String): List<League> {
        val filtered = leaguesRepository.leagues
            .filter {
                it.name.startsWith(searchText, ignoreCase = true) && searchText.isNotEmpty()
            }
        return filtered
    }
}