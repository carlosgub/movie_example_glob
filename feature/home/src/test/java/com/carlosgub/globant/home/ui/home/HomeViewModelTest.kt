package com.carlosgub.globant.home.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.carlosgub.globant.core.commons.DispatcherProvider
import com.carlosgub.globant.core.commons.sealed.GenericState
import com.carlosgub.globant.home.helpers.MainCoroutineRule
import com.carlosgub.globant.home.helpers.TestDispatcherProvider
import com.carlosgub.globant.home.helpers.movieModel
import com.carlosgub.globant.home.model.usecase.GetMoviesFromCacheUseCase
import com.carlosgub.globant.home.model.usecase.GetMoviesFromQueryUseCase
import com.carlosgub.globant.home.model.usecase.SignOutUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
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
    fun `login Anonymously success`() = runBlocking {
        val list = listOf(movieModel)
        every {
            getMoviesFromQueryUseCase.invoke(any())
        }.returns(
            flowOf(list)
        )
        val viewModel =
            HomeViewModel(
                getMoviesFromQueryUseCase,
                getMoviesFromCacheUseCase,
                signOutUseCase,
                dispatcherProvider
            )

        viewModel.queryFieldChange("agua")
        viewModel.uiState.test {
            assertEquals(GenericState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify { getMoviesFromQueryUseCase.invoke(any()) }
    }
}
