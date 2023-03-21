package com.carlosgub.globant.login.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.login.helpers.MainCoroutineRule
import com.carlosgub.globant.login.helpers.TestDispatcherProvider
import com.carlosgub.globant.login.model.usecase.LoginAnonymouslyUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithEmailAndPasswordUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithFacebookUseCase
import com.carlosgub.globant.login.model.usecase.LoginWithGoogleUseCase
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
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
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()


    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    @MockK
    lateinit var loginAnonymouslyUseCase: LoginAnonymouslyUseCase

    @MockK
    lateinit var loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase

    @MockK
    lateinit var loginWithFacebookUseCase: LoginWithFacebookUseCase

    @MockK
    lateinit var loginWithGoogleUseCase: LoginWithGoogleUseCase

    @MockK
    lateinit var googleCredential: AuthCredential
    @MockK
    lateinit var fbCredential: AccessToken

    private val message = "Error"

    @Test
    fun `login Anonymously success`() = runTest {

        every {
            loginAnonymouslyUseCase.invoke()
        }.returns(
            flowOf(true)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginAnonymously()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginAnonymouslyUseCase.invoke() }
    }

    @Test
    fun `login Anonymously failed`() = runTest {

        every {
            loginAnonymouslyUseCase.invoke()
        }.returns(
            flowOf(false)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginAnonymously()
        viewModel.uiState.test {
            assertEquals(
                GenericState.Error("El usuario o la contraseña estan incorrectos"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginAnonymouslyUseCase.invoke() }
    }

    @Test
    fun `login Anonymously error`() = runTest {

        every {
            loginAnonymouslyUseCase.invoke()
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginAnonymously()
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginAnonymouslyUseCase.invoke() }
    }

    @Test
    fun `login with email and password success`() = runTest {

        every {
            loginWithEmailAndPasswordUseCase.invoke(any(), any())
        }.returns(
            flowOf(true)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginWithEmailAndPassword()
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithEmailAndPasswordUseCase.invoke(any(), any()) }
    }

    @Test
    fun `login with email and password failed`() = runTest {

        every {
            loginWithEmailAndPasswordUseCase.invoke(any(), any())
        }.returns(
            flowOf(false)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginWithEmailAndPassword()
        viewModel.uiState.test {
            assertEquals(
                GenericState.Error("El usuario o la contraseña estan incorrectos"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithEmailAndPasswordUseCase.invoke(any(), any()) }
    }

    @Test
    fun `login with email and password error`() = runTest {

        every {
            loginWithEmailAndPasswordUseCase.invoke(any(), any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.loginWithEmailAndPassword()
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithEmailAndPasswordUseCase.invoke(any(), any()) }
    }

    @Test
    fun `login with google success`() = runTest {
        every {
            loginWithGoogleUseCase.invoke(any())
        }.returns(
            flowOf(true)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithGoogle(googleCredential)
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithGoogleUseCase.invoke(any()) }
    }

    @Test
    fun `login with google failed`() = runTest {
        every {
            loginWithGoogleUseCase.invoke(any())
        }.returns(
            flowOf(false)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithGoogle(googleCredential)
        viewModel.uiState.test {
            assertEquals(
                GenericState.Error("El usuario o la contraseña estan incorrectos"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithGoogleUseCase.invoke(any()) }
    }

    @Test
    fun `login with google error`() = runTest {
        every {
            loginWithGoogleUseCase.invoke(any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithGoogle(googleCredential)
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithGoogleUseCase.invoke(any()) }
    }

    @Test
    fun `login with facebook success`() = runTest {
        every {
            loginWithFacebookUseCase.invoke(any())
        }.returns(
            flowOf(true)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithFacebook(fbCredential)
        viewModel.uiState.test {
            assertEquals(GenericState.Success(true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithFacebookUseCase.invoke(any()) }
    }

    @Test
    fun `login with facebook failed`() = runTest {
        every {
            loginWithFacebookUseCase.invoke(any())
        }.returns(
            flowOf(false)
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithFacebook(fbCredential)
        viewModel.uiState.test {
            assertEquals(
                GenericState.Error("El usuario o la contraseña estan incorrectos"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithFacebookUseCase.invoke(any()) }
    }

    @Test
    fun `login with facebook error`() = runTest {
        every {
            loginWithFacebookUseCase.invoke(any())
        }.returns(
            flow {
                throw IllegalStateException(message)
            }
        )
        val viewModel =
            LoginViewModel(
                loginAnonymouslyUseCase,
                loginWithEmailAndPasswordUseCase,
                loginWithFacebookUseCase,
                loginWithGoogleUseCase,
                dispatcherProvider
            )
        viewModel.signWithFacebook(fbCredential)
        viewModel.uiState.test {
            assertEquals(GenericState.Error(message), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { loginWithFacebookUseCase.invoke(any()) }
    }
}
