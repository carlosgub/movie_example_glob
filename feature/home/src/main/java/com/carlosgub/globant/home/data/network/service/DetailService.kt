package com.carlosgub.globant.home.data.network.service

import com.carlosgub.globant.home.data.network.clients.DetailClient
import com.carlosgub.globant.home.data.network.response.MovieDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailService @Inject constructor(
    private val detailClient: DetailClient
) {
    suspend fun getMovieDetail(movieId: String): MovieDetailResponse =
        withContext(Dispatchers.IO) {
            val response = detailClient.getMovieDetail(movieId = movieId)
            response.body()!!
        }
}
