package com.sports.freesportssample.data.api.reponse

import com.sports.freesportssample.data.entity.TeamEntity
import kotlinx.serialization.Serializable

@Serializable
data class GetTeamsResponse(
    val teams: List<TeamEntity>?
)
