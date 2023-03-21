package com.carlosgub.globant.login.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.login.model.usecase.LoginAnonymouslyUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithEmailAndPasswordUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithFacebookUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithGoogleUseCase
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAnonymouslyUseCase: LoginAnonymouslyUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val loginWithFacebookUseCase: LoginWithFacebookUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val uiState: StateFlow<GenericState<Boolean>> = _uiState

    private val _emailField = MutableLiveData<String>()
    val emailField: LiveData<String> = _emailField

    private val _passwordField = MutableLiveData<String>()
    val passwordField: LiveData<String> = _passwordField

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    init {
        _uiState.value = GenericState.None
    }

    fun usernameFieldChange(usernameValue: String) {
        _emailField.value = usernameValue.trim()
        enableLogin(usernameValue, passwordField.value.orEmpty())
    }

    fun passwordFieldChange(passwordValue: String) {
        _passwordField.value = passwordValue
        enableLogin(emailField.value.orEmpty(), passwordValue)
    }

    private fun enableLogin(email: String, password: String) {
        _isLoginEnabled.value =
            Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

    fun loginAnonymously() {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = GenericState.Loading
            loginAnonymouslyUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    if (it) {
                        _uiState.value = GenericState.Success(it)
                    } else {
                        _uiState.value =
                            GenericState.Error("El usuario o la contraseña estan incorrectos")
                    }
                }
        }
    }

    fun loginWithEmailAndPassword() {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = GenericState.Loading
            loginWithEmailAndPasswordUseCase(
                emailField.value.orEmpty(),
                passwordField.value.orEmpty()
            )
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    if (it) {
                        _uiState.value = GenericState.Success(it)
                    } else {
                        _uiState.value =
                            GenericState.Error("El usuario o la contraseña estan incorrectos")
                    }
                }
        }
    }

    fun clearState() {
        _isLoginEnabled.value = false
        _passwordField.value = ""
        _emailField.value = ""
        _uiState.value = GenericState.None
    }

    fun signWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = GenericState.Loading
            loginWithGoogleUseCase(
                credential
            )
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    if (it) {
                        _uiState.value = GenericState.Success(it)
                    } else {
                        _uiState.value =
                            GenericState.Error("El usuario o la contraseña estan incorrectos")
                    }
                }
        }
    }

    fun signWithFacebook(credential: AccessToken) = viewModelScope.launch {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = GenericState.Loading
            loginWithFacebookUseCase(
                credential
            )
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    if (it) {
                        _uiState.value = GenericState.Success(it)
                    } else {
                        _uiState.value =
                            GenericState.Error("El usuario o la contraseña estan incorrectos")
                    }
                }
        }
    }
}