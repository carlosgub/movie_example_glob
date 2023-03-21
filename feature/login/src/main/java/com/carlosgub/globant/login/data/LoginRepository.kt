package com.carlosgub.globant.login.data

import com.carlosgub.globant.login.data.firebase.FirebaseLogin
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
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

    suspend fun signInWithGoogle(credential: AuthCredential): Boolean =
        withContext(Dispatchers.Default) {
            firebaseLogin.logInWithGoogle(credential)
        }

    suspend fun signInWithFacebook(credential: AccessToken): Boolean =
        withContext(Dispatchers.Default) {
            firebaseLogin.logInWithFacebook(credential)
        }
}
