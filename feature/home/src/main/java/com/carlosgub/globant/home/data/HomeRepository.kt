package com.carlosgub.globant.home.data

import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.database.dao.MovieDao
import com.carlosgub.globant.home.data.firebase.FirebaseHome
import com.carlosgub.globant.home.data.network.service.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: SearchService,
    private val movieDao: MovieDao,
    private val firebaseHome: FirebaseHome
) {
    suspend fun getMoviesFromQuery(query: String): List<MovieModel> =
        withContext(Dispatchers.Default) {
            movieDao.deleteMovies()
            api.getMoviesFromQuery(query).map { movie ->
                val cast = api.getCreditsFromMovie(movie.id)
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
}
