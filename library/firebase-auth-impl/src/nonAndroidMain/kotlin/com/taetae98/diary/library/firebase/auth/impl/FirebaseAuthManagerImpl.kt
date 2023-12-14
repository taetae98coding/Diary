package com.taetae98.diary.library.firebase.auth.impl

import com.taetae98.diary.library.firebase.auth.api.FirebaseAccount
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signInWithGoogleToken(idToken: String, accessToken: String?) {
        Firebase.auth.signInWithCredential(GoogleAuthProvider.credential(idToken, accessToken.orEmpty()))
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override fun getUser(): Flow<FirebaseAccount?> {
        return Firebase.auth.authStateChanged.map { user ->
            user?.let {
                FirebaseAccount(
                    uid = user.uid,
                    email = user.email,
                )
            }
        }
    }
}