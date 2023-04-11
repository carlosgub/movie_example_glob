package com.carlosgub.globant.home.model.usecase

import com.carlosgub.globant.core.commons.model.DetailScreenModel
import com.carlosgub.globant.core.commons.model.MovieModel
import com.carlosgub.globant.home.data.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(movieId: String): Flow<DetailScreenModel> = flow {
        emit(homeRepository.getMovieDetail(movieId))
    }
}
