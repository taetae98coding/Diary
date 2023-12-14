package com.taetae98.diary.domain.repository

public interface LoginRepository {
    public suspend fun signIn(idToken: String)
}