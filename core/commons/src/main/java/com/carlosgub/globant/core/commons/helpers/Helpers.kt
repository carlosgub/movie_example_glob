package com.carlosgub.globant.core.commons.helpers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_25
import kotlinx.coroutines.delay

fun <T> showLoading(
    uiState: GenericState<T>
): Boolean = when (uiState) {
    is GenericState.Error -> false
    GenericState.Loading -> true
    GenericState.None -> false
    is GenericState.Success -> false
}

@Composable
fun <T> ShowErrorUiState(
    uiState: GenericState<T>,
    modifier: Modifier = Modifier
) {
    if (uiState is GenericState.Error) {
        ShowError(message = uiState.message, modifier = modifier)
    }
}

@Composable
fun ShowError(
    message: String,
    modifier: Modifier
) {
    var isButtonVisible by remember { mutableStateOf(true) }
    var secondsToDisappear by remember { mutableStateOf(5) }
    AnimatedVisibility(
        isButtonVisible,
        modifier = modifier
            .shadow(elevation = spacing_25)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (text) = createRefs()
                Text(
                    text = message,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(text) {
                            linkTo(
                                start = parent.start,
                                startMargin = spacing_2,
                                end = parent.end,
                                endMargin = spacing_2
                            )
                            linkTo(
                                top = parent.top,
                                topMargin = spacing_1,
                                bottom = parent.bottom,
                                bottomMargin = spacing_1
                            )
                            width = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        while (secondsToDisappear > 0) {
            delay(1000)
            secondsToDisappear -= 1
        }
        isButtonVisible = false
    }
}


fun <T> getDataFromUiState(
    uiState: GenericState<T>
): T? =
    if (uiState is GenericState.Success) {
        uiState.item
    } else {
        null
    }
