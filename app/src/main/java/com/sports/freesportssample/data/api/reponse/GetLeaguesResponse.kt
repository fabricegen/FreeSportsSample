package com.sports.freesportssample.data.api.reponse

import com.sports.freesportssample.data.entity.LeagueEntity
import kotlinx.serialization.Serializable

@Serializable
data class GetLeaguesResponse(
    val leagues: List<LeagueEntity>
)
