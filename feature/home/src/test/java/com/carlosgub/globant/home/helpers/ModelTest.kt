package com.carlosgub.globant.home.helpers

import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.network.response.MovieResponse

val movieModel = MovieModel(
    id = 1,
    originalTitle = "test",
    title = "title",
    posterPath = "posterPath",
    backdropPath = "backdropPath",
    releaseDate = "releaseDate",
    voteAverage = 20.0,
    voteCount = 1000,
    runtime = null,
    overview = null,
    castList = listOf()
)

val movieResponse = MovieResponse(
    id = 1,
    originalTitle = "test",
    title = "title",
    posterPath = "posterPath",
    releaseDate = "releaseDate",
    voteAverage = 20.0,
    voteCount = 1000,
    backdropPath = "backdropPath",
    genreIds = listOf(),
    popularity = 0.0
)
