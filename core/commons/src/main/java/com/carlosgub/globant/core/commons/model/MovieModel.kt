package com.carlosgub.globant.core.commons.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MOVIES")
data class MovieModel(
    @PrimaryKey
    val id: Int,
    val originalTitle: String,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val runtime: String? = null,
    val overview: String? = null,
    val castList: List<CastModel>,
    val genres: List<GenresModel>
)
