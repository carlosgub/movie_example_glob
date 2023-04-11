package com.carlosgub.globant.home.data.network.clients

import com.carlosgub.globant.home.BuildConfig
import com.carlosgub.globant.home.data.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieClient {
    @GET("/3/movie/popular")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>
}
