package com.sports.freesportssample.data.api

import com.sports.freesportssample.data.api.reponse.GetLeaguesResponse
import com.sports.freesportssample.data.api.reponse.GetTeamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeSportsApiService {
    @GET("/api/v1/json/{apiKey}/all_leagues.php")
    suspend fun getAllLeagues(): GetLeaguesResponse

    @GET("/api/v1/json/{apiKey}/search_all_teams.php")
    suspend fun getAllTeamsForLeague(
        @Query("l") leagueName: String,
    ): GetTeamsResponse
}
