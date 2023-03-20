package com.carlosgub.globant.moviexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosgub.globant.core.commons.helpers.navigateAndReplaceStartRoute
import com.carlosgub.globant.home.ui.HomeFeature
import com.carlosgub.globant.home.ui.home.HomeViewModel
import com.carlosgub.globant.login.ui.LoginFeature
import com.carlosgub.globant.login.ui.login.LoginViewModel
import com.carlosgub.globant.login.ui.signup.SignUpViewModel
import com.carlosgub.globant.moviexample.ui.SplashScreen
import com.carlosgub.globant.moviexample.ui.SplashViewModel
import com.carlosgub.globant.theme.theme.MovieExpertGlobantExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashViewModel: SplashViewModel by viewModels()
        val loginViewModel: LoginViewModel by viewModels()
        val signUpViewModel: SignUpViewModel by viewModels()
        val homeViewModel: HomeViewModel by viewModels()
        setContent {
            MovieExpertGlobantExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navigationController by rememberUpdatedState(newValue = rememberNavController())
                    NavHost(navController = navigationController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(
                                viewModel = splashViewModel,
                                goToScreen = { route ->
                                    navigationController.navigateAndReplaceStartRoute(route)
                                }
                            )
                        }
                        composable("login") {
                            LoginFeature(
                                loginViewModel,
                                signUpViewModel,
                                goToHome = {
                                    navigationController.navigateAndReplaceStartRoute("home")
                                }
                            )
                        }
                        composable("home") {
                            HomeFeature(
                                signOut = {
                                    navigationController.navigateAndReplaceStartRoute("login")
                                },
                                homeViewModel = homeViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}