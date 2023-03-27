package com.carlosgub.globant.home.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.database.dao.MovieDao
import com.carlosgub.globant.home.data.firebase.FirebaseHome
import com.carlosgub.globant.home.data.network.response.MovieResponse
import com.carlosgub.globant.home.data.network.service.SearchService
import com.carlosgub.globant.home.helpers.MainCoroutineRule
import com.carlosgub.globant.home.helpers.movieModel
import com.carlosgub.globant.home.helpers.movieResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    lateinit var api: SearchService

    @MockK
    lateinit var movieDao: MovieDao

    @MockK
    lateinit var firebaseHome: FirebaseHome

    private val message = "Error"

    @Test
    fun `get movies from query successfully`() = runBlocking {
        val listResponse = listOf(movieResponse)
        val listModel = listOf(movieModel)
        coEvery {
            movieDao.deleteMovies()
        }.returns(
            Unit
        )
        coEvery {
            api.getCreditsFromMovie(any())
        }.returns(
            listOf()
        )
        coEvery {
            movieDao.addMovie(any())
        }.returns(
            Unit
        )
        coEvery {
            api.getMoviesFromQuery(any())
        }.returns(
            listResponse
        )
        val repository =
            HomeRepository(
                api,
                movieDao,
                firebaseHome
            )

        val response = repository.getMoviesFromQuery("marvel")
        assertEquals(listModel, response)
    }

    @Test
    fun `get movies from query successfully empty list`() = runBlocking {
        val listResponse = listOf<MovieResponse>()
        val listModel = listOf<MovieModel>()
        coEvery {
            movieDao.deleteMovies()
        }.returns(
            Unit
        )
        coEvery {
            api.getCreditsFromMovie(any())
        }.returns(
            listOf()
        )
        coEvery {
            movieDao.addMovie(any())
        }.returns(
            Unit
        )
        coEvery {
            api.getMoviesFromQuery(any())
        }.returns(
            listResponse
        )
        val repository =
            HomeRepository(
                api,
                movieDao,
                firebaseHome
            )

        val response = repository.getMoviesFromQuery("marvel")
        assertEquals(listModel, response)
    }
}
