package com.carlosgub.globant.login.ui.signup

import app.cash.turbine.test
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.login.helpers.TestDispatcherProvider
import com.carlosgub.globant.login.model.usecase.SignupUseCase
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
class SignUpViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var signupUseCase: SignupUseCase

    private val message = "Error"

    @Test
    fun `login Anonymously success`() = runTest {

        every {
            signupUseCase.invoke(any(), any(), any())
        }.returns(
            flowOf(true)
        )
        val viewModel =
            SignUpViewModel(
                signupUseCase,
                dispatcherProvider
            )
        viewModel.signUp()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { signupUseCase.invoke(any(),any(),any()) }
    }

    @Test
    fun `login Anonymously failed`() = runTest {

        every {
            signupUseCase.invoke(any(), any(), any())
        }.returns(
            flowOf(false)
        )
        val viewModel =
            SignUpViewModel(
                signupUseCase,
                dispatcherProvider
            )
        viewModel.signUp()
        viewModel.uiState.test {
            assertEquals(
                GenericState.Error("Hubo un problema en el registro"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { signupUseCase.invoke(any(),any(),any()) }
    }

    @Test
    fun `login Anonymously error`() = runTest {

        every {
            signupUseCase.invoke(any(), any(), any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            SignUpViewModel(
                signupUseCase,
                dispatcherProvider
            )
        viewModel.signUp()
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { signupUseCase.invoke(any(),any(),any()) }
    }
}
