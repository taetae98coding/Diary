package com.taetae98.diary.core.auth.api

import kotlinx.coroutines.flow.Flow

public interface AuthManager {
    public suspend fun signIn(credential: CredentialEntity)
    public suspend fun signOut()

    public fun getAccount(): Flow<AccountEntity>
}