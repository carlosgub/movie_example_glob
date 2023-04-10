package com.carlosgub.globant.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlosgub.globant.home.ui.detail.DetailScreen
import com.carlosgub.globant.home.ui.detail.DetailViewModel
import com.carlosgub.globant.home.ui.home.HomeScreen
import com.carlosgub.globant.home.ui.movie.MovieViewModel
import com.carlosgub.globant.home.ui.search.SearchViewModel

@Composable
fun HomeFeature(
    signOut: () -> Unit,
    movieViewModel: MovieViewModel,
    searchViewModel: SearchViewModel,
    detailViewModel: DetailViewModel,
    modifier: Modifier = Modifier
) {
    val navigationController by rememberUpdatedState(newValue = rememberNavController())
    NavHost(navController = navigationController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                signOut = signOut,
                movieViewModel = movieViewModel,
                searchViewModel = searchViewModel,
                modifier = modifier,
                goToDetail = {
                    navigationController.navigate("detail/$it")
                }
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            ),
            content = { backStackEntry ->
                DetailScreen(
                    viewModel = detailViewModel,
                    goBack = {
                        navigationController.popBackStack()
                    },
                    id = backStackEntry.arguments?.getInt("id") ?: 0
                )
            }
        )
    }

}