package com.carlosgub.globant.home.model.usecase

import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> = flow {
        emit(repository.getNowPlayingMovies())
    }
}
