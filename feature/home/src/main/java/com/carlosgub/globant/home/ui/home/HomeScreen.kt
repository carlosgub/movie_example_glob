package com.carlosgub.globant.home.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carlosgub.globant.core.commons.model.BottomBarScreen
import com.carlosgub.globant.home.ui.movie.MovieScreen
import com.carlosgub.globant.home.ui.movie.MovieViewModel
import com.carlosgub.globant.home.ui.play.PlayScreen
import com.carlosgub.globant.home.ui.profile.ProfileScreen
import com.carlosgub.globant.home.ui.profile.ProfileViewModel
import com.carlosgub.globant.home.ui.search.SearchScreen
import com.carlosgub.globant.home.ui.search.SearchViewModel
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_2_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_4_2
import com.carlosgub.globant.theme.theme.view_12

@Composable
fun HomeScreen(
    signOut: () -> Unit,
    movieViewModel: MovieViewModel,
    searchViewModel: SearchViewModel,
    profileViewModel: ProfileViewModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        modifier = modifier
    ) { paddingValues ->
        BottomNavGraph(
            movieViewModel = movieViewModel,
            navController = navController,
            searchViewModel = searchViewModel,
            profileViewModel = profileViewModel,
            goToDetail = goToDetail,
            signOut = signOut,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    ConstraintLayout(
        modifier = modifier
            .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(PrimaryColor)
            .fillMaxWidth()
    ) {
        val (movie, search, ticket, profile) = createRefs()

        AddItem(
            screen = BottomBarScreen.Movie,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier
                .constrainAs(movie) {
                    start.linkTo(parent.start, spacing_2_2)
                    end.linkTo(search.start)
                    linkTo(
                        top = parent.top,
                        topMargin = spacing_2,
                        bottomMargin = spacing_2,
                        bottom = parent.bottom
                    )
                }
        )
        AddItem(
            screen = BottomBarScreen.Search,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(search) {
                start.linkTo(movie.end, spacing_2_2)
                end.linkTo(ticket.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_2,
                    bottomMargin = spacing_2,
                    bottom = parent.bottom
                )
            }
        )
        AddItem(
            screen = BottomBarScreen.Play,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(ticket) {
                start.linkTo(search.end, spacing_2_2)
                end.linkTo(profile.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_2,
                    bottomMargin = spacing_2,
                    bottom = parent.bottom
                )
            }
        )
        AddItem(
            screen = BottomBarScreen.Profile,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(profile) {
                start.linkTo(ticket.end)
                end.linkTo(parent.end, spacing_2_2)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_2,
                    bottomMargin = spacing_2,
                    bottom = parent.bottom
                )
            }
        )
    }
}

@Composable
fun AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val contentColor =
        if (selected) Color.Black else Color.Black.copy(alpha = 0.3f)

    ConstraintLayout(
        modifier = modifier
            .height(view_12)
            .clip(CircleShape)
            .padding(
                start = spacing_2_2,
                end = spacing_2_2,
                top = spacing_2,
                bottom = spacing_2
            )
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        val (icon, text) = createRefs()
        Icon(
            imageVector = screen.icon,
            contentDescription = "icon",
            tint = contentColor,
            modifier = Modifier.constrainAs(icon) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top)
            }
        )
        AnimatedVisibility(
            visible = selected,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                linkTo(
                    top = icon.bottom,
                    bottom = parent.bottom
                )
            }
        ) {
            Text(
                text = screen.title,
                color = contentColor
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    profileViewModel: ProfileViewModel,
    searchViewModel: SearchViewModel,
    signOut: () -> Unit,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Movie.route,
        modifier = modifier
    ) {
        composable(route = BottomBarScreen.Movie.route) {
            MovieScreen(
                viewModel = movieViewModel,
                goToDetail = goToDetail
            )
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                goToDetail = goToDetail
            )
        }
        composable(route = BottomBarScreen.Play.route) {
            PlayScreen(
            )
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                viewModel = profileViewModel,
                signOut = signOut
            )
        }
    }
}
