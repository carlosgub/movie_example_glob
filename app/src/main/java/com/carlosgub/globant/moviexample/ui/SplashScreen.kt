package com.carlosgub.globant.moviexample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.helpers.navigateAndReplaceStartRoute
import com.carlosgub.globant.core.commons.helpers.showError
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.spacing_10

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    goToScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<Boolean>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    value = it
                }
            }
        }
    )
    val isUserLogged = getDataFromUiState(uiState)
    if (isUserLogged == true) {
        LaunchedEffect(Unit) {
            goToScreen("home")
        }
    } else if (isUserLogged == false) {
        LaunchedEffect(Unit) {
            goToScreen("login")
        }
    }
    val context = LocalContext.current
    showError(uiState, context)
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
    ) {
        val (logo) = createRefs()
        Image(
            painter = painterResource(id = com.carlosgub.globant.resources.R.drawable.ic_imdb_logo_168_84),
            contentDescription = "Logo",
            modifier = Modifier
                .constrainAs(logo) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )
    }
}