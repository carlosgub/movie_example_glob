package com.carlosgub.globant.core.commons.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    // for home
    object Movie : BottomBarScreen(
        route = "movie",
        title = "Inicio",
        icon = Icons.Filled.Home
    )

    // for search
    object Search : BottomBarScreen(
        route = "search",
        title = "Buscar",
        icon = Icons.Filled.Search
    )

    // for play
    object Play : BottomBarScreen(
        route = "play",
        title = "Play",
        icon = Icons.Filled.PlayCircle
    )

    // for profile
    object Profile : BottomBarScreen(
        route = "profile",
        title = "Perfil",
        icon = Icons.Filled.AccountCircle
    )
}
