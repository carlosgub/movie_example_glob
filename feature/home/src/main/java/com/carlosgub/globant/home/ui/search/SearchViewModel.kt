@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.carlosgub.globant.home.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.home.model.usecase.GetMoviesFromCacheUseCase
import com.carlosgub.globant.home.model.usecase.GetMoviesFromQueryUseCase
import com.carlosgub.globant.home.model.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase,
    private val getMoviesFromCacheUseCase: GetMoviesFromCacheUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    private val _uiState = MutableStateFlow<GenericState<List<MovieModel>>>(GenericState.None)
    val uiState: StateFlow<GenericState<List<MovieModel>>> = _uiState

    private val _uiStateSignOut = MutableStateFlow<GenericState<Boolean>>(GenericState.None)
    val uiStateSignOut: StateFlow<GenericState<Boolean>> = _uiStateSignOut

    init {
        search()
    }

    fun queryFieldChange(query: String) {
        if (query.isNotEmpty()) {
            _uiState.value = GenericState.Loading
        }
        _query.value = query
    }

    private fun search() {
        viewModelScope.launch(dispatcherProvider.main) {
            _query
                .asFlow()
                .flowOn(dispatcherProvider.io)
                .filter {
                    it.trim().isEmpty().not() && it.isNotEmpty()
                }
                .debounce(DEBOUNCE_TIME)
                .distinctUntilChanged()
                .flatMapLatest {
                    getMoviesFromQueryUseCase(it)
                }
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }

    fun getMoviesCache() {
        viewModelScope.launch(dispatcherProvider.main) {
            getMoviesFromCacheUseCase()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _uiState.value = GenericState.Error(it.message.orEmpty())
                }
                .collect {
                    _uiState.value = GenericState.Success(it)
                }
        }
    }

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
        _query.value = ""
        _uiState.value = GenericState.None
        _uiStateSignOut.value = GenericState.None
    }

    companion object {
        const val DEBOUNCE_TIME = 300L
    }
}
