package com.carlosgub.globant.login.data

import com.carlosgub.globant.login.data.firebase.FirebaseSignup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupRepository @Inject constructor(
    private val firebaseSignup: FirebaseSignup
) {

    suspend fun signup(name: String, email: String, password: String): Boolean =
        withContext(Dispatchers.Default) {
            firebaseSignup.signup(name, email, password)
        }
}
