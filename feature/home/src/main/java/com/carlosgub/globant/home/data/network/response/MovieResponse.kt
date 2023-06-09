package com.carlosgub.globant.home.data.network.response

import com.carlosgub.globant.core.commons.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: Int,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("genre_ids") val genreIds: List<Int>? = listOf()
) {
    fun toMovieModel(castList: List<CastResponse>): MovieModel {
        return MovieModel(
            id = id,
            originalTitle = originalTitle,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            castList = castList.map { it.toCastModel() },
            genres = listOf()
        )
    }
}
