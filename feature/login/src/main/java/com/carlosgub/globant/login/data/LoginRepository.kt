package com.carlosgub.globant.login.data

import com.carlosgub.globant.login.data.firebase.FirebaseLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val firebaseLogin: FirebaseLogin
) {
    suspend fun signInAnonymously(): Boolean =
        withContext(Dispatchers.Default) {
            firebaseLogin.logInAnonymously()
        }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean =
        withContext(Dispatchers.Default) {
            firebaseLogin.logInWithEmailAndPassword(email, password)
        }
}
