package com.carlosgub.globant.home.data.network.response

import com.carlosgub.globant.core.commons.model.CastModel

data class CastResponse(
    val name: String,
    val id: Int
) {
    fun toCastModel(): CastModel {
        return CastModel(
            id = id,
            name = name
        )
    }
}
