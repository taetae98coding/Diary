package com.taetae98.diary.library.firebase.auth.api

public interface FirebaseAuthManager {
    public suspend fun signIn(idToken: String)
}