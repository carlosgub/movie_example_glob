package com.carlosgub.globant.home.data.network.response

import com.carlosgub.globant.core.commons.model.CastModel
import com.google.gson.annotations.SerializedName

data class CastResponse(
    val name: String,
    val id: Int,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("profile_path") val profilePath: String?
) {
    fun toCastModel(): CastModel {
        return CastModel(
            id = id,
            name = name,
            profilePath = profilePath,
            originalName = originalName
        )
    }
}
