package com.carlosgub.globant.core.commons.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosgub.globant.core.commons.helpers.getImagePath
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.resources.R
import com.carlosgub.globant.theme.theme.TextFieldBackgroundColor
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6
import com.carlosgub.globant.theme.theme.view_4

@Composable
fun IMDBMovies(
    title: String,
    movies: List<MovieModel>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (spacer, textColor, rv) = createRefs()

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
        IMDBTitle(
            title = title,
            modifier = Modifier
                .constrainAs(textColor) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(spacer.bottom)
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
            items(movies) { movieModel ->
                IMDBMoviesItem(
                    movieModel,
                    goToDetail = goToDetail
                )
            }
        }
    }
}

@Composable
fun IMDBMoviesItem(
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
                text = movieModel.title,
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