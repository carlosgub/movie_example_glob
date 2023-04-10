package com.carlosgub.globant.core.commons.helpers

import androidx.navigation.NavHostController
import com.carlosgub.globant.core.commons.model.MovieModel

fun MovieModel.getImagePath(): String =
    if (posterPath != null) {
        "https://image.tmdb.org/t/p/w500$posterPath"
    } else {
        "https://i.stack.imgur.com/GNhx0.png"
    }

fun MovieModel.getBackgroundPath(): String =
    if (this.backdropPath != null) {
        "https://image.tmdb.org/t/p/w500${backdropPath}";
    } else {
        "https://i.stack.imgur.com/GNhx0.png";
    }

fun NavHostController.navigateAndReplaceStartRoute(newHomeRoute: String) {
    navigate(newHomeRoute) {
        popUpTo(this@navigateAndReplaceStartRoute.graph.id) {
            inclusive = true
        }
    }
}