package com.carlosgub.globant.home.data.network.clients

import com.carlosgub.globant.home.BuildConfig
import com.carlosgub.globant.home.data.network.response.CreditsResponse
import com.carlosgub.globant.home.data.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchClient {
    @GET("/3/search/movie")
    suspend fun getMoviesFromQuery(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1,
        @Query("query") query: String
    ): Response<MoviesResponse>


    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCreditsFromMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<CreditsResponse>
}
