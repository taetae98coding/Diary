package com.taetae98.diary.library.google.sign.api

public interface GoogleAuthManager {
    public suspend fun signIn(): GoogleCredential?
}