package com.sports.freesportssample.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class LeagueEntity(
    val idLeague: Int,
    val strLeague: String,
    val strSport: String,
    val strLeagueAlternate: String?
)
