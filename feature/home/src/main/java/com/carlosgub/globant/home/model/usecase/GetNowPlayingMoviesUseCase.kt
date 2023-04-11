package com.carlosgub.globant.home.model.usecase

import com.carlosgub.globant.core.commons.model.MovieScreenModel
import com.carlosgub.globant.home.data.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<MovieScreenModel> = flow {
        emit(repository.getNowPlayingMovies())
    }
}
