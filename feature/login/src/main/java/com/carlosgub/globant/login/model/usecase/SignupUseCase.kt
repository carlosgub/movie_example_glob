package com.carlosgub.globant.login.model.usecase

import com.carlosgub.globant.login.data.SignupRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) = flow {
        emit(signupRepository.signup(name, email, password))
    }

}
