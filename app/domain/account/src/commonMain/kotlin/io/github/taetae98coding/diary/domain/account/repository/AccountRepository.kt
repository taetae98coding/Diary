package io.github.taetae98coding.diary.domain.account.repository

import io.github.taetae98coding.diary.core.model.account.AccountToken
import kotlinx.coroutines.flow.Flow

public interface AccountRepository {
    public suspend fun join(email: String, password: String)
    public suspend fun save(email: String, token: AccountToken)
    public suspend fun clear()

    public fun fetchToken(email: String, password: String): Flow<AccountToken>
    public fun getEmail(): Flow<String?>
    public fun getUid(): Flow<String?>
}
