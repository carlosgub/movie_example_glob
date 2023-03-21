package com.carlosgub.globant.login.model.usecase

import com.carlosgub.globant.login.data.LoginRepository
import com.facebook.AccessToken
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithFacebookUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(accessToken: AccessToken) = flow {
        emit(loginRepository.signInWithFacebook(accessToken))
    }

}
