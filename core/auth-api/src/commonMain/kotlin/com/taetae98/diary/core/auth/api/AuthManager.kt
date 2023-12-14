package com.taetae98.diary.core.auth.api

public interface AuthManager {
    public suspend fun signIn(idToken: String)
}