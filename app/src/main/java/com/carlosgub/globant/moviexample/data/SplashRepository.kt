package com.carlosgub.globant.moviexample.data

import com.carlosgub.globant.moviexample.data.firebase.SplashFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val splashFirebase: SplashFirebase
) {
    suspend fun isUserLogged(): Boolean =
        withContext(Dispatchers.Default) {
            splashFirebase.isUserLogged()
        }
}
