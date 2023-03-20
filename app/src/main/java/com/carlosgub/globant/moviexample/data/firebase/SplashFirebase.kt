package com.carlosgub.globant.moviexample.data.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashFirebase @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun isUserLogged(): Boolean =
        withContext(Dispatchers.IO) {
            auth.currentUser != null
        }
}
