@file:OptIn(ExperimentalComposeUiApi::class)

package com.carlosgub.globant.home.ui.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.Loading
import com.carlosgub.globant.resources.R
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.SeparatorColor
import com.carlosgub.globant.theme.theme.TextFieldBackgroundColor
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_1_2
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6
import com.carlosgub.globant.theme.theme.view_4

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<List<MovieModel>>>(
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
            MovieContent(
                movieList = data.orEmpty(),
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

@Composable
fun MovieContent(
    movieList: List<MovieModel>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (mostPopular, listOfPopular) = createRefs()
        if (movieList.isNotEmpty()) {
            MostPopular(
                movieModel = movieList.first(),
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
            PopularList(
                movieList = movieList.subList(1, movieList.size),
                goToDetail = goToDetail,
                modifier = Modifier
                    .constrainAs(listOfPopular) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = mostPopular.bottom,
                            topMargin = spacing_6,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
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

@Composable
fun PopularList(
    movieList: List<MovieModel>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (spacer, textColor, text, rv) = createRefs()

        Divider(
            modifier = Modifier
                .background(TextFieldBackgroundColor)
                .height(view_4)
                .constrainAs(spacer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                }
        )
        Box(
            Modifier
                .clip(CircleShape)
                .width(spacing_1_2)
                .height(spacing_6)
                .background(PrimaryColor)
                .constrainAs(textColor) {
                    start.linkTo(parent.start, spacing_6)
                    top.linkTo(spacer.bottom, spacing_4)
                })

        Text(
            text = "Las mejores selecciones",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = textColor.end,
                    startMargin = spacing_2,
                    end = parent.end,
                    endMargin = spacing_6
                )
                top.linkTo(textColor.top)
                bottom.linkTo(textColor.bottom)
                width = Dimension.fillToConstraints
            }
        )
        LazyRow(
            contentPadding = PaddingValues(vertical = spacing_4),
            state = state,
            modifier = Modifier.constrainAs(rv) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_4,
                    end = parent.end,
                    endMargin = spacing_4
                )
                top.linkTo(textColor.bottom, spacing_4)
                width = Dimension.fillToConstraints
            }
        ) {
            items(movieList) { movieModel ->
                MoviePopularItem(
                    movieModel,
                    goToDetail = goToDetail
                )
                Divider(color = SeparatorColor)
            }
        }
    }
}

@Composable
fun MoviePopularItem(
    movieModel: MovieModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable { goToDetail(movieModel.id) }
            .padding(horizontal = spacing_2)
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (movieCard, plus, star, ranking, title, info) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieModel.getImagePath())
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "movie poster",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(160.dp)
                    .width(110.dp)
                    .constrainAs(movieCard) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top)
                    }
            )
            Image(
                painter = painterResource(
                    id = R.drawable.ic_plus_16_22
                ),
                alpha = 0.8f,
                contentDescription = "plus",
                modifier = Modifier
                    .constrainAs(plus) {
                        start.linkTo(movieCard.start, spacing_2)
                        top.linkTo(parent.top)
                    }
            )
            Image(
                painter = painterResource(
                    id = R.drawable.ic_star_12_12
                ),
                alpha = 0.8f,
                contentDescription = "star",
                modifier = Modifier
                    .constrainAs(star) {
                        start.linkTo(movieCard.start, spacing_2)
                        top.linkTo(movieCard.bottom, spacing_2)
                    }
            )
            Text(
                text = movieModel.voteAverage.toString(),
                textAlign = TextAlign.Start,
                maxLines = 2,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(ranking) {
                        linkTo(
                            start = plus.end,
                            startMargin = spacing_1,
                            end = movieCard.end
                        )
                        top.linkTo(
                            movieCard.bottom,
                            spacing_2
                        )
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = movieModel.originalTitle,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(title) {
                        linkTo(
                            start = movieCard.start,
                            startMargin = spacing_2,
                            end = movieCard.end
                        )
                        top.linkTo(
                            ranking.bottom,
                            spacing_1
                        )
                        width = Dimension.fillToConstraints
                    }
            )

            Image(
                painter = painterResource(
                    id = R.drawable.ic_info_14_14
                ),
                contentDescription = "info",
                modifier = Modifier
                    .constrainAs(info) {
                        end.linkTo(movieCard.end, spacing_2)
                        top.linkTo(title.bottom, spacing_2)
                        bottom.linkTo(parent.bottom, spacing_2)
                    }
            )
        }
    }
}
