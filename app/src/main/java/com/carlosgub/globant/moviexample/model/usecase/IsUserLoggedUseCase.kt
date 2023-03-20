package com.carlosgub.globant.moviexample.model.usecase

import com.carlosgub.globant.moviexample.data.SplashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsUserLoggedUseCase @Inject constructor(
    private val splashRepository: SplashRepository
) {
    operator fun invoke(): Flow<Boolean> =
        flow {
            emit(splashRepository.isUserLogged())
        }
}
