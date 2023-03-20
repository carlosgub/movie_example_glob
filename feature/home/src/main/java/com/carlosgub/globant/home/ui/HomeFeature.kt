package com.carlosgub.globant.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosgub.globant.home.ui.home.HomeScreen
import com.carlosgub.globant.home.ui.home.HomeViewModel

@Composable
fun HomeFeature(
    signOut: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "homeScreen") {
            composable(
                route = "homeScreen",
                content = {
                    HomeScreen(
                        signOut = signOut,
                        viewModel = homeViewModel
                    )
                }
            )
        }
    }
}