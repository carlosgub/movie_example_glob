package com.carlosgub.globant.home.data.network.response

data class MoviesResponse(
    val page: Int,
    val results: List<MovieResponse>
)
