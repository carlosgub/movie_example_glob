package com.carlosgub.globant.core.commons.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MOVIES")
data class MovieModel(
    @PrimaryKey
    val id: Int,
    val originalTitle: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val runtime: String? = null,
    val overview: String? = null,
    val castList: List<CastModel>
)
