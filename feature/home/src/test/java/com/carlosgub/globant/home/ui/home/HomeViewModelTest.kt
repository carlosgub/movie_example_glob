package com.carlosgub.globant.home.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.home.helpers.MainCoroutineRule
import com.carlosgub.globant.home.helpers.TestDispatcherProvider
import com.carlosgub.globant.home.helpers.movieModel
import com.carlosgub.globant.home.model.usecase.GetMoviesFromCacheUseCase
import com.carlosgub.globant.home.model.usecase.GetMoviesFromQueryUseCase
import com.carlosgub.globant.home.model.usecase.SignOutUseCase
import com.carlosgub.globant.home.ui.search.SearchViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()


    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var getMoviesFromCacheUseCase: GetMoviesFromCacheUseCase

    @MockK
    lateinit var getMoviesFromQueryUseCase: GetMoviesFromQueryUseCase

    @MockK
    lateinit var signOutUseCase: SignOutUseCase

    private val message = "Error"

    @Test
    fun `get Movies from cache success`() = runBlocking {
        val list = listOf(movieModel)
        every {
            getMoviesFromCacheUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.getMoviesCache()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesFromCacheUseCase.invoke() }
    }

    @Test
    fun `get Movies from cache empty`() = runBlocking {
        val list = listOf<MovieModel>()
        every {
            getMoviesFromCacheUseCase.invoke()
        }.returns(
            flowOf(list)
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.getMoviesCache()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(list), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesFromCacheUseCase.invoke() }
    }

    @Test
    fun `get Movies from cache error`() = runBlocking {
        every {
            getMoviesFromCacheUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.getMoviesCache()
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesFromCacheUseCase.invoke() }
    }

    @Test
    fun `sign out success`() = runBlocking {
        every {
            signOutUseCase.invoke()
        }.returns(
            flowOf(true)
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.signOut()
        viewModel.uiStateSignOut.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { signOutUseCase.invoke() }
    }

    @Test
    fun `sign out error`() = runBlocking {
        every {
            signOutUseCase.invoke()
        }.returns(
            flowOf(false)
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.signOut()
        viewModel.uiStateSignOut.test {
            assertEquals(
                GenericState.Error("Hubo un problema al querer hacer el logout"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { signOutUseCase.invoke() }
    }

    @Test
    fun `sign out error throw`() = runBlocking {
        every {
            signOutUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            SearchViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.signOut()
        viewModel.uiStateSignOut.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { signOutUseCase.invoke() }
    }
}
