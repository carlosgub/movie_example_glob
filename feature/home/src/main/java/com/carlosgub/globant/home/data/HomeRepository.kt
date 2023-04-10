package com.carlosgub.globant.home.data

import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.database.dao.MovieDao
import com.carlosgub.globant.home.data.firebase.FirebaseHome
import com.carlosgub.globant.home.data.network.service.DetailService
import com.carlosgub.globant.home.data.network.service.MovieService
import com.carlosgub.globant.home.data.network.service.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val searchService: SearchService,
    private val movieService: MovieService,
    private val detailService: DetailService,
    private val movieDao: MovieDao,
    private val firebaseHome: FirebaseHome
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieModel> =
        withContext(Dispatchers.Default) {
            movieDao.deleteMovies()
            searchService.getMoviesFromQuery(query).map { movie ->
                val cast = searchService.getCreditsFromMovie(movie.id)
                async {
                    val movieModel = movie.toMovieModel(cast)
                    movieDao.addMovie(movieModel)
                    movieModel
                }
            }.awaitAll()
        }

    suspend fun getCacheMovies(): List<MovieModel> =
        withContext(Dispatchers.Default) {
            movieDao.getMovies()
        }

    suspend fun signOut(): Boolean =
        withContext(Dispatchers.Default) {
            firebaseHome.signOut()
        }

    suspend fun getNowPlayingMovies(): List<MovieModel> =
        withContext(Dispatchers.Default) {
            movieService.getNowPlayingMovies().map {
                async {
                    it.toMovieModel(listOf())
                }
            }.awaitAll()
        }

    suspend fun getMovieDetail(movieId: String): MovieModel =
        withContext(Dispatchers.Default) {
            val cast = searchService.getCreditsFromMovie(movieId = movieId.toInt())
            detailService.getMovieDetail(movieId).toMovieModel(cast)
        }
}
