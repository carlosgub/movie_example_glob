package com.carlosgub.globant.home.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.model.PostItModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.IMDBTitle
import com.carlosgub.globant.resources.R
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.SeparatorColor
import com.carlosgub.globant.theme.theme.TextDetailColor
import com.carlosgub.globant.theme.theme.TextFieldBackgroundColor
import com.carlosgub.globant.theme.theme.buttonNoElevation
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_3
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    signOut: () -> Unit,
    modifier: Modifier = Modifier
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state = rememberScrollState()
    val uiStateSignOut by produceState<GenericState<Boolean>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiStateSignOut.collect {
                    value = it
                }
            }
        }
    )
    val data = getDataFromUiState(uiStateSignOut)
    if (data == true) {
        viewModel.clearState()
        LaunchedEffect(Unit) {
            signOut()
        }
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (profileHeader, postIt, followList, recientViewedList, favouritePeople, profileOptions) = createRefs()
        ProfileHeader(
            modifier = Modifier.constrainAs(profileHeader) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )
        ProfilePostIt(
            modifier = Modifier.constrainAs(postIt) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(profileHeader.bottom)
                width = Dimension.fillToConstraints
            }
        )
        FollowList(
            listOf(),
            modifier = Modifier.constrainAs(followList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(postIt.bottom)
                width = Dimension.fillToConstraints
            }
        )
        RecientViewed(
            listOf(),
            modifier = Modifier.constrainAs(recientViewedList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(followList.bottom)
                width = Dimension.fillToConstraints
            }
        )
        FavouritePeople(
            modifier = Modifier.constrainAs(favouritePeople) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(recientViewedList.bottom)
                width = Dimension.fillToConstraints
            }
        )
        ProfileOptions(
            signOut = {
                viewModel.signOut()
            },
            modifier = Modifier.constrainAs(profileOptions) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(favouritePeople.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.wrapContent
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (profileImage, profileName, settingsIcon, divider) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://storage.googleapis.com/afs-prod/media/51c21ab48b5540ad96c58e2714a38df8/3000.jpeg")
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .constrainAs(profileImage) {
                    start.linkTo(parent.start, spacing_6)
                    top.linkTo(parent.top, spacing_4)
                }
        )
        Text(
            text = "Juan Perez",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(profileName) {
                    start.linkTo(profileImage.end, spacing_4)
                    end.linkTo(settingsIcon.start, spacing_4)
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                    width = Dimension.fillToConstraints
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_settings_21_21),
            contentDescription = "Logo",
            modifier = Modifier
                .constrainAs(settingsIcon) {
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                }
        )

        Divider(
            modifier = Modifier
                .background(TextFieldBackgroundColor)
                .height(0.5.dp)
                .constrainAs(divider) {
                    linkTo(
                        start = profileImage.start,
                        end = settingsIcon.end,
                    )
                    top.linkTo(profileImage.bottom, spacing_4)
                    width = Dimension.fillToConstraints

                }
        )

    }
}

@Composable
fun ProfilePostIt(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val postItList = listOf(
            PostItModel(
                title = "Calificaciones",
                comment = "Calificar y obtener recomendaciones",
                count = 0
            ),
            PostItModel(
                title = "Listas",
                comment = "Agregar a listas",
                count = 4
            ),
            PostItModel(
                title = "Comentarios",
                comment = "Aún no hay criticas",
                count = 0
            )
        )
        val state = rememberLazyListState()
        val (rv) = createRefs()
        LazyRow(
            contentPadding = PaddingValues(horizontal = spacing_4),
            state = state,
            modifier = Modifier.constrainAs(rv) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top, spacing_4)
                bottom.linkTo(parent.bottom, spacing_3)
                width = Dimension.fillToConstraints
            }
        ) {
            items(postItList) { postItModel ->
                PostItItem(
                    postItModel
                )
            }
        }
    }
}

@Composable
fun PostItItem(
    postItModel: PostItModel,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(horizontal = spacing_2)
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (background, comment, title, count) = createRefs()
            Box(
                modifier = Modifier
                    .background(TextFieldBackgroundColor)
                    .height(80.dp)
                    .width(114.dp)
                    .constrainAs(background) {
                        linkTo(
                            start = parent.start,
                            startMargin = spacing_2,
                            end = parent.end,
                            endMargin = spacing_2
                        )
                        top.linkTo(parent.top, spacing_2)
                    }
            )
            Text(
                text = postItModel.comment,
                fontSize = 10.sp,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(comment) {
                    linkTo(
                        start = background.start,
                        startMargin = spacing_2,
                        end = background.end,
                        endMargin = spacing_2
                    )
                    linkTo(
                        top = background.top,
                        topMargin = spacing_2,
                        bottom = background.bottom,
                        bottomMargin = spacing_2
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
            Text(
                text = postItModel.title,
                fontSize = 10.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_2,
                        end = parent.end,
                        endMargin = spacing_2
                    )
                    top.linkTo(background.bottom, spacing_1)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = postItModel.count.toString(),
                fontSize = 10.sp,
                color = TextDetailColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(count) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_2,
                        end = parent.end,
                        endMargin = spacing_2
                    )
                    top.linkTo(title.bottom, spacing_1)
                    bottom.linkTo(parent.bottom, spacing_1)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Composable
fun FollowList(
    followList: List<MovieModel>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, followTitle, rv, followEmptyText, followEmptyButton) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        IMDBTitle(
            title = "Lista de seguimiento",
            modifier = Modifier.constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
        )
        if (followList.isEmpty()) {
            Text(
                text = "Crear una lista de seguimiento para no perderte ninguna película o serie de tv.",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.constrainAs(followEmptyText) {
                    start.linkTo(parent.start, spacing_6)
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(followTitle.bottom, spacing_4)
                    width = Dimension.fillToConstraints
                }
            )
            Button(
                onClick = {

                },
                elevation = buttonNoElevation,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
                modifier = Modifier
                    .constrainAs(followEmptyButton) {
                        linkTo(
                            start = parent.start,
                            startMargin = spacing_2,
                            end = parent.end,
                            endMargin = spacing_2
                        )
                        top.linkTo(followEmptyText.bottom, spacing_6)
                        bottom.linkTo(parent.bottom, spacing_6)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Text(
                    text = "Empieza tu lista de seguimiento",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = PrimaryColor,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(
                            vertical = spacing_4
                        )
                )
            }
        }
    }
}

@Composable
fun RecientViewed(
    recientViewedList: List<MovieModel>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (recientSeparator, recientViewedTitle, recientRV, recientViewedEmptyText) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(recientSeparator) {
                    top.linkTo(parent.top)
                }
        )
        IMDBTitle(
            title = "Vistas recientes",
            modifier = Modifier.constrainAs(recientViewedTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(recientSeparator.bottom)
            }
        )
        if (recientViewedList.isEmpty()) {
            Text(
                text = "No has visitado ninguna página recientemente.",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.constrainAs(recientViewedEmptyText) {
                    start.linkTo(parent.start, spacing_6)
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(recientViewedTitle.bottom, spacing_4)
                    bottom.linkTo(parent.bottom, spacing_6)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}


@Composable
fun FavouritePeople(
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, followTitle, followEmptyText, followEmptyButton) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        IMDBTitle(
            title = "Personas favoritas",
            modifier = Modifier.constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
        )
        Text(
            text = "Añadir actores y directores favoritos y más para conocer las últimas novedades.",
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.constrainAs(followEmptyText) {
                start.linkTo(parent.start, spacing_6)
                end.linkTo(parent.end, spacing_6)
                top.linkTo(followTitle.bottom, spacing_4)
                width = Dimension.fillToConstraints
            }
        )
        Button(
            onClick = {

            },
            elevation = buttonNoElevation,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .constrainAs(followEmptyButton) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_2,
                        end = parent.end,
                        endMargin = spacing_2
                    )
                    top.linkTo(followEmptyText.bottom, spacing_6)
                    bottom.linkTo(parent.bottom, spacing_6)
                    width = Dimension.fillToConstraints
                }
        ) {
            Text(
                text = "Agregar personas favoritas",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PrimaryColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(
                        vertical = spacing_4
                    )
            )
        }
    }
}

@Composable
fun ProfileOptions(
    modifier: Modifier,
    signOut: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, signout, signoutIcon, secondSeparator) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        Text(text = "Cerrar sesión",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = spacing_6, vertical = spacing_4)
                .clickable { signOut() }
                .constrainAs(signout) {
                    top.linkTo(separator.bottom)
                    linkTo(start = parent.start, end = parent.end)
                    width = Dimension.fillToConstraints
                }
        )
        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = "icon",
            tint = SeparatorColor,
            modifier = Modifier
                .constrainAs(signoutIcon) {
                    end.linkTo(parent.end, spacing_6)
                    top.linkTo(signout.top)
                    bottom.linkTo(signout.bottom)
                }
        )
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(secondSeparator) {
                    top.linkTo(signout.bottom)
                }
        )
    }
}
