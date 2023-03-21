package com.carlosgub.globant.moviexample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.moviexample.model.usecase.IsUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedUseCase: IsUserLoggedUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val uiState: StateFlow<GenericState<Boolean>> = _uiState

    fun isUserLogged() {
        viewModelScope.launch(dispatcherProvider.main) {
            isUserLoggedUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }
}
