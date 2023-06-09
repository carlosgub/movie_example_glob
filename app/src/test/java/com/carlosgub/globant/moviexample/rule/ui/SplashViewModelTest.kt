package com.carlosgub.globant.moviexample.rule.ui

import app.cash.turbine.test
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.moviexample.model.usecase.IsUserLoggedUseCase
import com.carlosgub.globant.moviexample.rule.ui.helpers.TestDispatcherProvider
import com.carlosgub.globant.moviexample.ui.SplashViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SplashViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var isUserLoggedUseCase: IsUserLoggedUseCase

    private val message = "Error"

    @Test
    fun `the user is not logged`() = runTest {

        every {
            isUserLoggedUseCase.invoke()
        }.returns(
            flowOf(false)
        )
        val viewModel =
            SplashViewModel(isUserLoggedUseCase, dispatcherProvider)
        viewModel.isUserLogged()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { isUserLoggedUseCase.invoke() }
    }

    @Test
    fun `the usser is logged`() = runTest {
        every {
            isUserLoggedUseCase.invoke()
        }.returns(
            flowOf(true)
        )
        val viewModel =
            SplashViewModel(isUserLoggedUseCase, dispatcherProvider)
        viewModel.isUserLogged()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { isUserLoggedUseCase.invoke() }
    }

    @Test
    fun `splash error`() = runTest {
        every {
            isUserLoggedUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            SplashViewModel(isUserLoggedUseCase, dispatcherProvider)
        viewModel.isUserLogged()
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { isUserLoggedUseCase.invoke() }
    }
}
