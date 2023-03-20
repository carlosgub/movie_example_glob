package com.carlosgub.globant.login.data.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignup @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun signup(name: String, email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
                .await()
            val user = FirebaseAuth.getInstance().currentUser
            user != null
        } catch (e: Exception) {
            false
        }
    }
}
