package com.carlosgub.globant.home.data.network.service

import com.carlosgub.globant.home.data.network.clients.MovieClient
import com.carlosgub.globant.home.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieService @Inject constructor(
    private val movieClient: MovieClient
) {
    suspend fun getPopular(): List<MovieResponse> =
        withContext(Dispatchers.IO) {
            val response = movieClient.getNowPlayingMovies()
            response.body()?.results ?: listOf()
        }
    suspend fun getTopRated(): List<MovieResponse> =
        withContext(Dispatchers.IO) {
            val response = movieClient.getTopRated()
            response.body()?.results ?: listOf()
        }
}
