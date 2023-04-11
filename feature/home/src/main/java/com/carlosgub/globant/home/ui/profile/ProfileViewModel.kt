@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.carlosgub.globant.home.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.home.model.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {


    private val _uiStateSignOut = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val uiStateSignOut: StateFlow<GenericState<Boolean>> = _uiStateSignOut

    fun signOut() {
        viewModelScope.launch(dispatcherProvider.main) {
            signOutUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiStateSignOut.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    if (it) {
                        _uiStateSignOut.value = GenericState.Success(it)
                    } else {
                        _uiStateSignOut.value =
                            GenericState.Error("Hubo un problema al querer hacer el logout")
                    }
                }
        }
    }

    fun clearState() {
        _uiStateSignOut.value = GenericState.None
    }
}
