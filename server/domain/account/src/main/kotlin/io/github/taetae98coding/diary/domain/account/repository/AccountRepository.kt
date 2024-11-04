package io.github.taetae98coding.diary.domain.account.repository

import io.github.taetae98coding.diary.core.model.Account

public interface AccountRepository {
	public suspend fun contains(email: String): Boolean

	public suspend fun upsert(account: Account)

	public suspend fun findByEmail(email: String, password: String): Account?
}
