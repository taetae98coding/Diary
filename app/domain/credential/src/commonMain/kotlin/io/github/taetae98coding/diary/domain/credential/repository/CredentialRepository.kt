package io.github.taetae98coding.diary.domain.credential.repository

import io.github.taetae98coding.diary.core.model.account.AccountToken
import kotlinx.coroutines.flow.Flow

public interface CredentialRepository {
	public suspend fun join(email: String, password: String)

	public suspend fun save(email: String, token: AccountToken)

	public suspend fun clear()

	public fun fetchToken(email: String, password: String): Flow<AccountToken>
}
