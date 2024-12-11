package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.database.AccountTable
import io.github.taetae98coding.diary.core.model.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

	override fun findByEmail(email: String, password: String): Flow<Account?> = flow {
		newSuspendedTransaction { AccountTable.findByEmail(email, password) }
			.also { emit(it) }
	}

	override fun findByUid(uid: String): Flow<Account?> = flow {
		newSuspendedTransaction { AccountTable.findByUid(uid) }
			.also { emit(it) }
	}
}
