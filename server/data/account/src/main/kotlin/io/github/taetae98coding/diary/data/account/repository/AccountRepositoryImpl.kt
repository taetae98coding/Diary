package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.database.AccountTable
import io.github.taetae98coding.diary.core.model.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Singleton

@Singleton
internal class AccountRepositoryImpl : AccountRepository {
	override suspend fun contains(email: String): Boolean = newSuspendedTransaction { AccountTable.contains(email) }

	override suspend fun upsert(account: Account) {
		newSuspendedTransaction {
			AccountTable.insert(account)
		}
	}

	override suspend fun findByEmail(email: String, password: String): Account? = newSuspendedTransaction { AccountTable.findByEmail(email, password) }
}
