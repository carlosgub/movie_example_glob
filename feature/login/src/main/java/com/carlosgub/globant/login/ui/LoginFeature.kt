package com.carlosgub.globant.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlosgub.globant.login.ui.login.LoginScreen
import com.carlosgub.globant.login.ui.login.LoginViewModel
import com.carlosgub.globant.login.ui.signup.SignupScreen
import com.carlosgub.globant.login.ui.signup.SignUpViewModel

@Composable
fun LoginFeature(
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    goToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "loginScreen") {
            composable(
                route = "loginScreen",
                content = {
                    LoginScreen(
                        viewModel = loginViewModel,
                        navController = navController,
                        goToHome = goToHome
                    )
                }
            )
            composable(
                route = "signupScreen",
                content = {
                    SignupScreen(
                        viewModel = signUpViewModel,
                        navController = navController,
                        goToHome = goToHome
                    )
                }
            )
        }
    }
}