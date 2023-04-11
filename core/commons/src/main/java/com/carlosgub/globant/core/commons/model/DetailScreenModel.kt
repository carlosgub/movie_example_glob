package com.carlosgub.globant.core.commons.model

data class DetailScreenModel(
    val detail: MovieModel,
    val recommendation: List<MovieModel>
)
