package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountRepositoryImpl(
	private val preferencesDataSource: AccountPreferences,
) : AccountRepository {
	override fun getEmail(): Flow<String?> = preferencesDataSource.getEmail()

	override fun getUid(): Flow<String?> = preferencesDataSource.getUid()
}
