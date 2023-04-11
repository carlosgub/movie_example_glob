package com.carlosgub.globant.home.data.network.response

import com.carlosgub.globant.core.commons.model.MovieModel
import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("genres") val genres: List<CategoriesResponse>?
) {
    fun toMovieModel(castList: List<CastResponse>): MovieModel =
        MovieModel(
            id = id,
            originalTitle = originalTitle,
            title = title,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            overview = overview,
            castList = castList.map { it.toCastModel() },
            backdropPath = backdropPath,
            genres = genres?.map { it.toGenresModel() } ?: listOf()
        )
}
