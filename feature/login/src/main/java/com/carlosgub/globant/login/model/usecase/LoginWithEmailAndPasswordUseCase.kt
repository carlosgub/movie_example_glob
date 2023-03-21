package com.carlosgub.globant.login.model.usecase

import com.carlosgub.globant.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, password: String) = flow {
        emit(loginRepository.signInWithEmailAndPassword(email, password))
    }

}
