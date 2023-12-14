package com.taetae98.diary.domain.repository

import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.account.Credential
import kotlinx.coroutines.flow.Flow

public interface AccountRepository {
    public suspend fun signIn(credential: Credential)
    public suspend fun signOut()

    public fun getAccount(): Flow<Account>
}