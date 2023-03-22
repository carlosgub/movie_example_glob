@file:OptIn(ExperimentalComposeUiApi::class)

package com.carlosgub.globant.login.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.carlosgub.globant.core.commons.helpers.ShowErrorUiState
import com.carlosgub.globant.core.commons.helpers.getDataFromUiState
import com.carlosgub.globant.core.commons.helpers.showLoading
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.core.commons.views.IMDbButton
import com.carlosgub.globant.core.commons.views.Loading
import com.carlosgub.globant.resources.R
import com.carlosgub.globant.theme.theme.BorderColor
import com.carlosgub.globant.theme.theme.LoadingBackgroundColor
import com.carlosgub.globant.theme.theme.OutlineTextFieldStyleLabel
import com.carlosgub.globant.theme.theme.OutlineTextFieldStylePlaceholder
import com.carlosgub.globant.theme.theme.TextColor
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_10
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_3
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6

@Composable
fun SignupScreen(
    viewModel: SignUpViewModel,
    navController: NavController,
    goToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nameText by viewModel.nameField.observeAsState(initial = "")
    val emailText by viewModel.emailField.observeAsState(initial = "")
    val passwordText by viewModel.passwordField.observeAsState(initial = "")
    val isSignupEnabled by viewModel.isSignupEnabled.observeAsState(initial = false)
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<GenericState<Boolean>>(
        initialValue = GenericState.None,
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
    val data = getDataFromUiState(uiState = uiState)
    if (data == true) {
        viewModel.clearState()
        LaunchedEffect(Unit) {
            goToHome()
        }
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val focusManager = LocalFocusManager.current
        val (
            back,
            logo,
            signUpTitle,
            nameTextField,
            emailTextField,
            passwordTextField,
            hintPasswordText,
            signupButton,
            loading,
            error
        ) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back_24_24),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(spacing_2)
                .clickable {
                    navController.popBackStack()
                }
                .constrainAs(back) {
                    start.linkTo(
                        parent.start,
                        spacing_4
                    )
                    top.linkTo(parent.top, spacing_2)
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_imdb_logo_168_84),
            contentDescription = "Logo",
            modifier = Modifier
                .height(48.dp)
                .constrainAs(logo) {
                    start.linkTo(
                        parent.start,
                        spacing_10
                    )
                    top.linkTo(back.bottom, spacing_2)
                }
        )
        Text(
            text = "Crear una cuenta",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .height(48.dp)
                .constrainAs(signUpTitle) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        endMargin = spacing_10,
                        end = parent.end
                    )
                    top.linkTo(logo.bottom, spacing_6)
                    width = Dimension.fillToConstraints
                }
        )
        NameOutlinedTextField(
            queryValue = nameText,
            onKeyboardClick = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            modifier = Modifier
                .testTag("sign_up_name")
                .constrainAs(nameTextField) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        endMargin = spacing_10,
                        end = parent.end
                    )
                    top.linkTo(signUpTitle.bottom, spacing_2)
                    width = Dimension.fillToConstraints
                }
        ) {
            viewModel.nameFieldChange(it)
        }
        EmailOutlinedTextField(
            queryValue = emailText,
            onKeyboardClick = {
                focusManager.moveFocus(FocusDirection.Next)
            },
            modifier = Modifier
                .testTag("sign_up_email")
                .constrainAs(emailTextField) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        endMargin = spacing_10,
                        end = parent.end
                    )
                    top.linkTo(nameTextField.bottom, spacing_3)
                    width = Dimension.fillToConstraints
                }
        ) {
            viewModel.emailFieldChange(it)
        }
        PasswordOutlinedTextField(
            queryValue = passwordText,
            onKeyboardClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                if (isSignupEnabled) {
                    viewModel.signUp()
                }
            },
            modifier = Modifier
                .testTag("sign_up_password")
                .constrainAs(passwordTextField) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        endMargin = spacing_10,
                        end = parent.end
                    )
                    top.linkTo(emailTextField.bottom, spacing_3)
                    width = Dimension.fillToConstraints
                }
        ) {
            viewModel.passwordFieldChange(it)
        }

        Text(
            text = "La contraseña debe contener 8 caracteres",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(hintPasswordText) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_10,
                    endMargin = spacing_10,
                    end = parent.end
                )
                top.linkTo(passwordTextField.bottom, spacing_1)
                width = Dimension.fillToConstraints
            }
        )

        IMDbButton(
            onClick = {
                viewModel.signUp()
            },
            testName = "sign_up_button",
            isEnabled = isSignupEnabled,
            text = stringResource(id = R.string.sign_up_sign_up_button),
            modifier = Modifier
                .constrainAs(signupButton) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        endMargin = spacing_6,
                        end = parent.end
                    )
                    top.linkTo(hintPasswordText.bottom, spacing_4)
                    width = Dimension.fillToConstraints
                }
        )
        if (showLoading(uiState)) {
            Loading(
                modifier = Modifier
                    .background(
                        LoadingBackgroundColor.copy(
                            alpha = 0.5f
                        )
                    )
                    .constrainAs(loading) {
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
        }
        ShowErrorUiState(
            uiState,
            Modifier
                .testTag("sign_up_error")
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
fun NameOutlinedTextField(
    queryValue: String,
    onKeyboardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        label = {
            Text(
                text = "Nombre",
                style = OutlineTextFieldStyleLabel
            )
        },
        placeholder = {
            Text(
                text = "",
                style = OutlineTextFieldStylePlaceholder
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = TextColor,
            focusedIndicatorColor = BorderColor,
            unfocusedIndicatorColor = BorderColor
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onKeyboardClick()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}

@Composable
fun EmailOutlinedTextField(
    queryValue: String,
    onKeyboardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        label = {
            Text(
                text = "Correo electrónico",
                style = OutlineTextFieldStyleLabel
            )
        },
        placeholder = {
            Text(
                text = "",
                style = OutlineTextFieldStylePlaceholder
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = TextColor,
            focusedIndicatorColor = BorderColor,
            unfocusedIndicatorColor = BorderColor
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onKeyboardClick()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}

@Composable
fun PasswordOutlinedTextField(
    queryValue: String,
    onKeyboardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var passwordIcon by rememberSaveable { mutableStateOf(true) }
    OutlinedTextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        label = {
            Text(
                text = "Contraseña",
                style = OutlineTextFieldStyleLabel
            )
        },
        placeholder = {
            Text(
                text = "",
                style = OutlineTextFieldStylePlaceholder
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = TextColor,
            focusedIndicatorColor = BorderColor,
            unfocusedIndicatorColor = BorderColor
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onKeyboardClick()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            Icon(
                imageVector = if (passwordIcon) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                contentDescription = "Mostrar contraseña",
                modifier = Modifier.clickable {
                    passwordIcon = !passwordIcon
                },
                tint = Color.Black
            )
        },
        visualTransformation = if (passwordIcon) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    )
}