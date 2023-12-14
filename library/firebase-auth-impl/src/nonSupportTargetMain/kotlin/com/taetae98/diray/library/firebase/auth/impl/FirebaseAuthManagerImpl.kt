package com.taetae98.diray.library.firebase.auth.impl

import com.taetae98.diary.library.firebase.auth.api.FirebaseAuthManager
import com.taetae98.diary.library.firebase.auth.api.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FirebaseAuthManagerImpl : FirebaseAuthManager {
    override suspend fun signInWithGoogleToken(idToken: String, accessToken: String?) {

    }

    override suspend fun signOut() {

    }

    override fun getUser(): Flow<FirebaseUser?> {
        return emptyFlow()
    }
}