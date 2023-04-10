package com.carlosgub.globant.login.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignup @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {

    suspend fun signup(name: String, email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
                .await()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                firestore.collection("USERS").document(user.uid)
                    .set(mutableMapOf<String, Any?>().apply {
                        set("name", name)
                    }).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
