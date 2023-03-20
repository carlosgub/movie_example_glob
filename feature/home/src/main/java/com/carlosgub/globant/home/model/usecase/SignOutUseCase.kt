package com.carlosgub.globant.home.model.usecase

import com.carlosgub.globant.home.data.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        emit(homeRepository.signOut())
    }
}
