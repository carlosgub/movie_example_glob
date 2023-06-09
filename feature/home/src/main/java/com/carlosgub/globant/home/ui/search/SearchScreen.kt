@file:OptIn(ExperimentalComposeUiApi::class)

package com.carlosgub.globant.home.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.carlosgub.globant.core.commons.helpers.ShowErrorUiState
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.helpers.getImagePath
import com.carlosgub.globant.core.commons.helpers.showLoading
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.Loading
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.SeparatorColor
import com.carlosgub.globant.theme.theme.TextFieldBackgroundColor
import com.carlosgub.globant.theme.theme.TextFieldStyle
import com.carlosgub.globant.theme.theme.TextFieldTextColor
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_10
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_3
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6
import com.carlosgub.globant.theme.theme.view_18
import com.carlosgub.globant.theme.theme.view_25
import com.carlosgub.globant.theme.theme.view_50
import com.carlosgub.globant.theme.theme.view_6
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val keyboardController = LocalSoftwareKeyboardController.current
    val queryValue: String by viewModel.query.observeAsState(initial = "")
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
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LaunchedEffect("cache movies") {
            viewModel.getMoviesCache()
        }
        val (searchBar, searchContent, fab, error) = createRefs()
        SearchTextField(
            queryValue = queryValue,
            keyboardController = keyboardController,
            onValueChange = {
                viewModel.queryFieldChange(it)
            },
            modifier = Modifier
                .testTag("home_search")
                .constrainAs(searchBar) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        end = parent.end,
                        endMargin = spacing_6
                    )
                    top.linkTo(parent.top, spacing_4)
                    width = Dimension.fillToConstraints
                }
        )
        SearchContent(
            uiState = uiState,
            queryValue = queryValue,
            goToDetail = goToDetail,
            keyboardController = keyboardController,
            modifier = Modifier.constrainAs(searchContent) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                linkTo(
                    top = searchBar.bottom,
                    topMargin = spacing_4,
                    bottom = parent.bottom
                )
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )
        ShowErrorUiState(
            uiState,
            modifier = Modifier
                .testTag("home_error")
                .constrainAs(error) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    bottom.linkTo(parent.bottom, spacing_4)
                    width = Dimension.fillToConstraints
                }
        )
    }
}


@Composable
fun SearchTextField(
    queryValue: String,
    keyboardController: SoftwareKeyboardController?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextFieldTextColor
            )
        },
        singleLine = true,
        maxLines = 1,
        textStyle = TextFieldStyle,
        placeholder = {
            Text(
                text = "Search Movie ...",
                style = TextFieldStyle
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = TextFieldBackgroundColor,
            textColor = TextFieldTextColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    )
}

@Composable
fun SearchContent(
    uiState: GenericState<List<MovieModel>>,
    queryValue: String,
    keyboardController: SoftwareKeyboardController?,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.background(TextFieldBackgroundColor)) {
        val (loading, topSeparator, listRef, noMovies) = createRefs()
        if (showLoading(uiState) && queryValue.isNotEmpty()) {
            Loading(
                modifier = Modifier.constrainAs(loading) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        } else {
            val list = getDataFromUiState(uiState) ?: listOf()
            val state = rememberLazyListState()
            val scroll = remember { derivedStateOf { state.firstVisibleItemScrollOffset } }
            if (scroll.value > 0) {
                keyboardController?.hide()
            }
            if (list.isEmpty() && queryValue.isNotEmpty()) {
                ShowNoMoviesFound(
                    modifier = Modifier
                        .constrainAs(noMovies) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            linkTo(
                                top = parent.top,
                                bottom = parent.bottom
                            )
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
            } else if (list.isNotEmpty()) {
                Divider(color = SeparatorColor,
                    modifier = Modifier
                        .height(1.dp)
                        .constrainAs(topSeparator) {
                            linkTo(
                                start = parent.start,
                                startMargin = spacing_6,
                                endMargin = spacing_6,
                                end = parent.end
                            )
                            top.linkTo(parent.top, spacing_4)
                            width = Dimension.fillToConstraints
                        }
                )
                LazyVerticalMovies(
                    list = list.toImmutableList(),
                    goToDetail = goToDetail,
                    state = state,
                    contentPaddingValues = PaddingValues(bottom = spacing_3),
                    modifier = Modifier.constrainAs(listRef) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = topSeparator.bottom,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}

@Composable
fun LazyVerticalMovies(
    list: ImmutableList<MovieModel>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState,
    contentPaddingValues: PaddingValues = PaddingValues(vertical = spacing_3),
) {
    LazyColumn(
        modifier = modifier
            .testTag("home_lazy_column")
            .padding(horizontal = spacing_6),
        contentPadding = contentPaddingValues,
        state = state
    ) {
        items(list) { movieModel ->
            MovieBookedItem(movieModel, goToDetail = goToDetail)
            Divider(color = SeparatorColor)
        }
    }
}

@Composable
fun MovieBookedItem(
    movieModel: MovieModel,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clickable { goToDetail(movieModel.id) }
            .fillMaxWidth()
            .padding(all = spacing_3)
    ) {
        val (movieCard, title, year, crew) = createRefs()
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .height(view_25)
                .width(view_18)
                .constrainAs(movieCard) {
                    start.linkTo(parent.start)
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieModel.getImagePath())
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(com.carlosgub.globant.resources.R.drawable.placeholder),
                contentDescription = "movie poster",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
            )
        }
        Text(
            text = movieModel.originalTitle,
            textAlign = TextAlign.Start,
            maxLines = 2,
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(title) {
                    linkTo(
                        start = movieCard.end,
                        startMargin = spacing_2,
                        end = parent.end
                    )
                    top.linkTo(
                        movieCard.top,
                        spacing_2
                    )
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = movieModel.releaseDate?.split("-")?.first().orEmpty(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(year) {
                    linkTo(
                        start = movieCard.end,
                        startMargin = spacing_2,
                        end = parent.end
                    )
                    top.linkTo(
                        title.bottom,
                        spacing_1
                    )
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = movieModel.castList.joinToString(separator = ",") {
                it.name
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.overline,
            modifier = Modifier
                .constrainAs(crew) {
                    linkTo(
                        start = movieCard.end,
                        startMargin = spacing_2,
                        end = parent.end
                    )
                    top.linkTo(
                        year.bottom,
                        spacing_2
                    )
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun ShowNoMoviesFound(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .testTag("home_no_movies")
    ) {
        val (lottie, spacer, text) = createRefs()
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.carlosgub.globant.resources.R.raw.no_found))
        val progressLottie by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { progressLottie },
            modifier = Modifier
                .height(view_50)
                .constrainAs(lottie) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = spacer.top
                    )
                }
        )
        Spacer(
            modifier = Modifier
                .size(view_6)
                .constrainAs(spacer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = lottie.bottom,
                        bottom = text.top
                    )
                }
        )
        Text(
            text = "No Movies Found",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = spacer.bottom,
                        bottom = parent.bottom
                    )
                }
        )
        createVerticalChain(lottie, spacer, text, chainStyle = ChainStyle.Packed)
    }
}


@Composable
fun MyFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = { onClick() },
        backgroundColor = PrimaryColor,
        contentColor = Color.White,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "SignOut")
    }
}