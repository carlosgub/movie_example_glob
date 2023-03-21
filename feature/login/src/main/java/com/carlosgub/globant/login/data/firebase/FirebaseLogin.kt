package com.carlosgub.globant.login.data.firebase

import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseLogin @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun logInAnonymously(): Boolean {
        return try {
            auth.signInAnonymously()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logInWithEmailAndPassword(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password)
                .await()
            val user = FirebaseAuth.getInstance().currentUser
            user != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logInWithGoogle(credential: AuthCredential): Boolean {
        return try {
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun logInWithFacebook(credential: AccessToken): Boolean {
        val facebookCredential = FacebookAuthProvider.getCredential(credential.token)
        return try {
            auth.signInWithCredential(facebookCredential).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
