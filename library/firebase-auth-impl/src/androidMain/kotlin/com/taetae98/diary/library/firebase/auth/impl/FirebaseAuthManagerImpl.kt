package com.taetae98.diary.library.firebase.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.taetae98.diary.library.firebase.auth.api.FirebaseAccount
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signInWithGoogleToken(idToken: String, accessToken: String?) {
        val googleCredential = GoogleAuthProvider.getCredential(idToken, accessToken)

        FirebaseAuth.getInstance().signInWithCredential(googleCredential).await()
    }

    override suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getUser(): Flow<FirebaseAccount?> {
        return callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                val user = it.currentUser
                if (user == null) {
                    trySend(null)
                } else {
                    trySend(FirebaseAccount(user.uid, user.email))
                }
            }

            FirebaseAuth.getInstance().addAuthStateListener(listener)
            awaitClose {
                FirebaseAuth.getInstance().removeAuthStateListener(listener)
            }
        }
    }
}