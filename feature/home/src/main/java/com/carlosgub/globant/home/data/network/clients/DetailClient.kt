package com.carlosgub.globant.home.data.network.clients

import com.carlosgub.globant.home.BuildConfig
import com.carlosgub.globant.home.data.network.response.MovieDetailResponse
import com.carlosgub.globant.home.data.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailClient {
    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetailResponse>
    @GET("/3/movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<MoviesResponse>
}
