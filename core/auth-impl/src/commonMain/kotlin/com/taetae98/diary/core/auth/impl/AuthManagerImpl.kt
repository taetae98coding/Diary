package com.taetae98.diary.core.auth.impl

import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import org.koin.core.annotation.Factory

@Factory
internal class AuthManagerImpl(
    private val firebaseAuthManager: FirebaseAuthManager,
) : AuthManager {
    override suspend fun signIn(idToken: String) {
        firebaseAuthManager.signIn(idToken)
    }
}