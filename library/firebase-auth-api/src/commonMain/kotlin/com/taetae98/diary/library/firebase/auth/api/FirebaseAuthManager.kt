package com.taetae98.diary.library.firebase.auth.api

import kotlinx.coroutines.flow.Flow

public interface FirebaseAuthManager {
    public suspend fun signInWithGoogleToken(idToken: String, accessToken: String?)
    public suspend fun signOut()

    public fun getUser(): Flow<FirebaseUser?>
}