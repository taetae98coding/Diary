package com.taetae98.diary.library.firebase.auth.impl

import com.taetae98.diary.library.firebase.auth.api.FirebaseAccount
import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signInWithGoogleToken(idToken: String, accessToken: String?) {

    }

    override suspend fun signOut() {

    }

    override fun getUser(): Flow<FirebaseAccount?> {
        return flowOf(null)
    }
}