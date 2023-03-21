package com.carlosgub.globant.login.model.usecase

import com.carlosgub.globant.login.data.LoginRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(authCredential: AuthCredential) = flow {
        emit(loginRepository.signInWithGoogle(authCredential))
    }

}
