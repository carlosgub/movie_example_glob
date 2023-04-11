package com.carlosgub.globant.home.data.network.response

import com.carlosgub.globant.core.commons.model.GenresModel

data class CategoriesResponse(
    val id: Int,
    val name: String
) {
    fun toGenresModel(): GenresModel {
        return GenresModel(
            id = id,
            name = name
        )
    }
}
