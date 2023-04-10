package com.carlosgub.globant.home.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.helpers.showLoading
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.Loading
import com.carlosgub.globant.theme.theme.SeparatorColor
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_10
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    goBack: () -> Unit,
    id: Int,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<MovieModel>>(
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

    viewModel.getMovieDetail(id.toString())

    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (loading, content, booking) = createRefs()
        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier.constrainAs(
                    loading
                ) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
            )
        } else {
            getDataFromUiState(uiState)?.let { movieModel ->
                MovieDetailContent(
                    movie = movieModel,
                    goBack = goBack,
                    modifier = Modifier.constrainAs(content) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun MovieDetailContent(
    movie: MovieModel,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(state)
    ) {
        val (toolbar, time, poster, textContent) = createRefs()
        MovieDetailToolbar(
            movie.title,
            goBack = goBack,
            modifier = Modifier.constrainAs(toolbar) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun MovieDetailToolbar(
    title: String,
    modifier: Modifier,
    goBack: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        val (back, titleRef, divider) = createRefs()
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "icon",
            tint = Color.Black,
            modifier = Modifier
                .clickable { goBack() }
                .constrainAs(back) {
                    start.linkTo(parent.start, spacing_4)
                    top.linkTo(parent.top, spacing_2)
                }
        )

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(titleRef) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_10,
                    end = parent.end,
                    endMargin = spacing_10
                )
                top.linkTo(parent.top, spacing_2)
            }
        )

        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(1.dp)
                .constrainAs(divider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}
