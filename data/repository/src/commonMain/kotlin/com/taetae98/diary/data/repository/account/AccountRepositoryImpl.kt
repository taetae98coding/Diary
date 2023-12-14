package com.taetae98.diary.data.repository.account

import com.taetae98.diary.core.auth.api.AccountEntity
import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.domain.entity.account.Account
import com.taetae98.diary.domain.entity.account.Credential
import com.taetae98.diary.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class AccountRepositoryImpl(
    private val authManager: AuthManager,
) : AccountRepository {
    override suspend fun signIn(credential: Credential) {
        authManager.signIn(credential.toEntity())
    }

    override suspend fun signOut() {
        authManager.signOut()
    }

    override fun getAccount(): Flow<Account> {
        return authManager.getAccount().map(AccountEntity::toDomain)
    }
}