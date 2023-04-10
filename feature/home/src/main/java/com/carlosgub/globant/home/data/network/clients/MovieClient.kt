package com.carlosgub.globant.home.data.network.clients

import com.carlosgub.globant.home.BuildConfig
import com.carlosgub.globant.home.data.network.response.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieClient {
    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>
}
