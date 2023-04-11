@file:OptIn(ExperimentalComposeUiApi::class)

package com.carlosgub.globant.home.ui.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosgub.globant.core.commons.helpers.getBackgroundPath
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.helpers.getImagePath
import com.carlosgub.globant.core.commons.helpers.showLoading
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.model.MovieScreenModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.IMDBMovies
import com.carlosgub.globant.core.commons.views.Loading
import com.carlosgub.globant.resources.R
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<MovieScreenModel>>(
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

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (progress, body) = createRefs()
        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier.constrainAs(progress) {
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
            val data = getDataFromUiState(uiState)
            data?.let {
                MovieContent(
                    movieScreenModel = data,
                    goToDetail = goToDetail,
                    modifier = Modifier
                        .constrainAs(body) {
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
fun MovieContent(
    movieScreenModel: MovieScreenModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (mostPopular, popular, topRated) = createRefs()
        if (movieScreenModel.popular.isNotEmpty()) {
            MostPopular(
                movieModel = movieScreenModel.popular.first(),
                modifier = Modifier
                    .constrainAs(mostPopular) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top)
                    },
                goToDetail = goToDetail
            )
            IMDBMovies(
                title = "Las mejores selecciones",
                movies = movieScreenModel.popular.subList(1, movieScreenModel.popular.size),
                goToDetail = goToDetail,
                modifier = Modifier
                    .constrainAs(popular) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(mostPopular.bottom, spacing_6)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
            IMDBMovies(
                title = "Favoritos de los aficionados",
                movies = movieScreenModel.topRated,
                goToDetail = goToDetail,
                modifier = Modifier
                    .constrainAs(topRated) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = popular.bottom,
                            topMargin = spacing_1,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
        }

    }
}

@Composable
fun MostPopular(
    movieModel: MovieModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .clickable { goToDetail(movieModel.id) }) {
        val (movieBackground, moviePoster, movieName, movieDescription) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieModel.getBackgroundPath())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(240.dp)
                .constrainAs(movieBackground) {
                    top.linkTo(parent.top)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieModel.getImagePath())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(140.dp)
                .width(100.dp)
                .constrainAs(moviePoster) {
                    top.linkTo(movieBackground.top, 160.dp)
                    start.linkTo(parent.start, spacing_6)
                }
        )

        Text(text = movieModel.originalTitle,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            modifier = Modifier
                .constrainAs(movieName) {
                    top.linkTo(movieBackground.bottom, spacing_2)
                    linkTo(
                        start = moviePoster.end,
                        startMargin = spacing_4,
                        endMargin = spacing_4,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                })

        Text(text = "Trailer Oficial",
            style = MaterialTheme.typography.overline,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(movieDescription) {
                    top.linkTo(movieName.bottom, spacing_1)
                    linkTo(
                        start = moviePoster.end,
                        startMargin = spacing_4,
                        endMargin = spacing_4,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                })
    }
}
