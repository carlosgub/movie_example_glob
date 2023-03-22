@file:OptIn(ExperimentalComposeUiApi::class)

package com.carlosgub.globant.login.ui.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
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
import com.carlosgub.globant.login.R
import com.carlosgub.globant.theme.theme.LoadingBackgroundColor
import com.carlosgub.globant.theme.theme.PrimaryColor
import com.carlosgub.globant.theme.theme.TextColor
import com.carlosgub.globant.theme.theme.TextFieldStyle
import com.carlosgub.globant.theme.theme.spacing_1
import com.carlosgub.globant.theme.theme.spacing_10
import com.carlosgub.globant.theme.theme.spacing_2
import com.carlosgub.globant.theme.theme.spacing_4
import com.carlosgub.globant.theme.theme.spacing_6
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
    goToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val emailText by viewModel.emailField.observeAsState(initial = "")
    val passwordText by viewModel.passwordField.observeAsState(initial = "")
    val isLoginEnabled by viewModel.isLoginEnabled.observeAsState(initial = false)
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val state = rememberScrollState()
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
    val token = stringResource(R.string.default_web_client_id)

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
            .background(PrimaryColor)
    ) {
        val data = getDataFromUiState(uiState = uiState)
        if (data == true) {
            viewModel.clearState()
            LaunchedEffect(Unit) {
                goToHome()
            }
        }
        val (
            logo,
            userTitle,
            userTextField,
            passwordTitle,
            passwordTextField,
            forgetPasswordButton,
            loginButton,
            alternativeLogin,
            signUpText,
            guestSignUp,
            loading,
            error
        ) = createRefs()
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                    viewModel.signWithGoogle(credential)
                } catch (e: ApiException) {
                    Log.d(":)", e.message.orEmpty())
                }
            }
        val focusManager = LocalFocusManager.current
        Image(
            painter = painterResource(id = com.carlosgub.globant.resources.R.drawable.ic_imdb_logo_168_84),
            contentDescription = "Back",
            modifier = Modifier
                .height(100.dp)
                .constrainAs(logo) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(parent.top, 100.dp)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "Usuario",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(userTitle) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(logo.bottom, spacing_4)
                    width = Dimension.fillToConstraints
                }
        )
        EmailTextField(
            queryValue = emailText,
            onKeyboardClick = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onValueChange = {
                viewModel.usernameFieldChange(it)
            },
            modifier = Modifier
                .testTag("login_email")
                .constrainAs(userTextField) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(userTitle.bottom, spacing_2)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "Contraseña",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(passwordTitle) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(userTextField.bottom, spacing_4)
                    width = Dimension.fillToConstraints
                }
        )
        PasswordTextField(
            queryValue = passwordText,
            onKeyboardClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                if (isLoginEnabled) {
                    viewModel.loginWithEmailAndPassword()
                }
            },
            onValueChange = {
                viewModel.passwordFieldChange(it)
            },
            modifier = Modifier
                .testTag("login_password")
                .constrainAs(passwordTextField) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(passwordTitle.bottom, spacing_2)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = "¿Olvidaste la contraseña?",
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(vertical = spacing_1)
                .constrainAs(forgetPasswordButton) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(passwordTextField.bottom)
                    width = Dimension.fillToConstraints
                }
        )
        IMDbButton(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                viewModel.loginWithEmailAndPassword()
            },
            testName = "login_button",
            isEnabled = isLoginEnabled,
            text = stringResource(id = com.carlosgub.globant.resources.R.string.login_login),
            modifier = Modifier
                .constrainAs(loginButton) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        end = parent.end,
                        endMargin = spacing_6
                    )
                    top.linkTo(forgetPasswordButton.bottom, spacing_6)
                    width = Dimension.fillToConstraints
                }
        )

        AlternativeLoginContainer(
            appleClick = {
                Toast.makeText(context, "No hay login con Apple por el momento", Toast.LENGTH_SHORT)
                    .show()
            },
            googleClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            },
            loginFacebook = {
                viewModel.signWithFacebook(it.accessToken)
            },
            modifier = Modifier
                .constrainAs(alternativeLogin) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(loginButton.bottom, spacing_6)
                    width = Dimension.fillToConstraints
                }
        )
        SignUpContainer(onClick = {
            navController.navigate("signupScreen")
        }, modifier = Modifier
            .constrainAs(signUpText) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_10,
                    end = parent.end,
                    endMargin = spacing_10
                )
                top.linkTo(alternativeLogin.bottom, spacing_6)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = stringResource(id = com.carlosgub.globant.resources.R.string.login_guest_signin),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = spacing_1)
                .clickable {
                    viewModel.loginAnonymously()
                }
                .constrainAs(guestSignUp) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(signUpText.bottom, spacing_4)
                    bottom.linkTo(parent.bottom, spacing_6)
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
                .testTag("login_error")
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
fun EmailTextField(
    queryValue: String,
    onKeyboardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        textStyle = TextFieldStyle,
        placeholder = {
            Text(
                text = "",
                style = TextFieldStyle
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = TextColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
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
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    )
}

@Composable
fun PasswordTextField(
    queryValue: String,
    onKeyboardClick: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordIcon by rememberSaveable { mutableStateOf(true) }
    TextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = true,
        maxLines = 1,
        textStyle = TextFieldStyle,
        placeholder = {
            Text(
                text = "",
                style = TextFieldStyle
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            textColor = TextColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
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
                contentDescription = stringResource(id = com.carlosgub.globant.resources.R.string.login_see_password),
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
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
    )
}


@Composable
fun AlternativeLoginContainer(
    appleClick: () -> Unit,
    googleClick: () -> Unit,
    loginFacebook: (LoginResult) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (alternativeLoginTitle, appleLogin, facebookLogin, googleLogin) = createRefs()

        Text(text = stringResource(id = com.carlosgub.globant.resources.R.string.login_alternative_title),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(alternativeLoginTitle) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_10,
                    end = parent.end,
                    endMargin = spacing_10
                )
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )

        Image(
            painter = painterResource(id = com.carlosgub.globant.resources.R.drawable.ic_apple_logo_22_27),
            contentDescription = stringResource(id = com.carlosgub.globant.resources.R.string.login_apple_logo),
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color.White,
                    MaterialTheme.shapes.large
                )
                .clickable { appleClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .constrainAs(appleLogin) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_10,
                        end = facebookLogin.start,
                        endMargin = spacing_1
                    )
                    top.linkTo(alternativeLoginTitle.bottom, spacing_6)
                }
        )
        CustomFacebookButton(
            loginFacebook = {
                loginFacebook(it)
            },
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color.White,
                    MaterialTheme.shapes.large
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .constrainAs(facebookLogin) {
                    linkTo(
                        start = appleLogin.end,
                        startMargin = spacing_1,
                        end = googleLogin.start,
                        endMargin = spacing_1
                    )
                    top.linkTo(alternativeLoginTitle.bottom, spacing_6)
                }
        )

        Image(
            painter = painterResource(id = com.carlosgub.globant.resources.R.drawable.ic_google_logo_27_27),
            contentDescription = stringResource(id = com.carlosgub.globant.resources.R.string.login_google_logo),
            modifier = Modifier
                .size(48.dp)
                .clickable { googleClick() }
                .background(
                    Color.White,
                    MaterialTheme.shapes.large
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .constrainAs(googleLogin) {
                    linkTo(
                        start = facebookLogin.end,
                        startMargin = spacing_1,
                        end = parent.end,
                        endMargin = spacing_10
                    )
                    top.linkTo(alternativeLoginTitle.bottom, spacing_6)
                }
        )
    }
}

@Composable
fun SignUpContainer(
    onClick: () -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .testTag("login_sign_in_button")
            .clickable {
                onClick()
            }) {
        val (firstText, spacer, secondText) = createRefs()
        Text(text = stringResource(id = com.carlosgub.globant.resources.R.string.login_signup_first_text),
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.constrainAs(firstText) {
                linkTo(
                    start = parent.start,
                    end = spacer.start
                )
                top.linkTo(parent.top)
            }
        )
        Spacer(
            modifier = Modifier
                .size(4.dp)
                .constrainAs(spacer) {
                    linkTo(
                        start = firstText.end,
                        end = secondText.start
                    )
                    top.linkTo(parent.top)
                })

        Text(
            text = stringResource(id = com.carlosgub.globant.resources.R.string.login_signup_second_text),
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .constrainAs(secondText) {
                    linkTo(
                        start = spacer.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                }
        )
        createHorizontalChain(firstText, spacer, secondText, chainStyle = ChainStyle.Packed)
    }
}

@Composable
fun CustomFacebookButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loginFacebook: (LoginResult) -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = ::LoginButton,
        update = { button ->
            button.setPermissions("email")
            button.isEnabled = enabled
        }
    )
}
