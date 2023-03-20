package com.carlosgub.globant.login.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.login.model.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val uiState: StateFlow<GenericState<Boolean>> = _uiState

    private val _nameField = MutableLiveData<String>()
    val nameField: LiveData<String> = _nameField

    private val _emailField = MutableLiveData<String>()
    val emailField: LiveData<String> = _emailField

    private val _passwordField = MutableLiveData<String>()
    val passwordField: LiveData<String> = _passwordField

    private val _isSignupEnabled = MutableLiveData<Boolean>()
    val isSignupEnabled: LiveData<Boolean> = _isSignupEnabled

    fun nameFieldChange(nameValue: String) {
        _nameField.value = nameValue
        enableLogin()
    }

    fun emailFieldChange(usernameValue: String) {
        _emailField.value = usernameValue.trim()
        enableLogin()
    }

    fun passwordFieldChange(passwordValue: String) {
        _passwordField.value = passwordValue
        enableLogin()
    }

    private fun enableLogin() {
        _isSignupEnabled.value =
            Patterns.EMAIL_ADDRESS.matcher(
                (emailField.value.orEmpty())
            ).matches() &&
                    Pattern.compile("(?=[A-Za-z0-9@#\$%^&+!=]+\$)^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%^&+!=])(?=.{8,}).*\$")
                        .matcher(
                            passwordField.value.orEmpty()
                        ).matches()
        nameField.value.orEmpty().isNotEmpty()
    }

    fun signUp() {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = GenericState.Loading
            signupUseCase(
                nameField.value.orEmpty(),
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
                            GenericState.Error("Hubo un problema en el registro")
                    }
                }
        }
    }


    fun clearState() {
        _isSignupEnabled.value = false
        _nameField.value = ""
        _passwordField.value = ""
        _emailField.value = ""
        _uiState.value = GenericState.None
    }
}