package com.carlosgub.globant.login.model.usecase

import com.carlosgub.globant.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginAnonymouslyUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke() = flow {
        emit(loginRepository.signInAnonymously())
    }

}
