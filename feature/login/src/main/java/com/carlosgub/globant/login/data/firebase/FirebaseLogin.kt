package com.carlosgub.globant.login.data.firebase

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

    /*
    fun recoverPassword(email: String): Completable {
        return Completable.create { completable ->
            if (isConnected(completable)) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            completable.onComplete()
                        } else {
                            completable.onError(Throwable(RECOVER_PASSWORD_ERROR_FIREBASE))
                        }
                    }
            }
        }
    }*/
}
