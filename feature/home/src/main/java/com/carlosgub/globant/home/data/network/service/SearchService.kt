package com.carlosgub.globant.home.data.network.service

import com.carlosgub.globant.home.data.network.clients.SearchClient
import com.carlosgub.globant.home.data.network.response.CastResponse
import com.carlosgub.globant.home.data.network.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchService @Inject constructor(
    private val searchClient: SearchClient
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieResponse> =
        withContext(Dispatchers.IO) {
            val response = searchClient.getMoviesFromQuery(query = query)
            response.body()?.results ?: listOf()
        }

    suspend fun getCreditsFromMovie(movieId: Int): List<CastResponse> =
        withContext(Dispatchers.IO) {
            val response = searchClient.getCreditsFromMovie(movieId = movieId)
            response.body()?.cast ?: listOf()
        }
}
