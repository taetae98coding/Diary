package io.github.taetae98coding.diary.domain.account.repository

import io.github.taetae98coding.diary.core.model.Account
import kotlinx.coroutines.flow.Flow

public interface AccountRepository {
	public suspend fun contains(email: String): Boolean

	public suspend fun upsert(account: Account)

	public fun findByEmail(email: String, password: String): Flow<Account?>
    public fun findByUid(uid: String): Flow<Account?>
}
