package io.github.taetae98coding.diary.data.credential.repository

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import io.github.taetae98coding.diary.core.diary.service.account.AccountService
import io.github.taetae98coding.diary.core.model.account.AccountToken
import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
internal class CredentialRepositoryImpl(private val preferencesDataSource: AccountPreferences, private val remoteDataSource: AccountService) : CredentialRepository {
	override suspend fun join(email: String, password: String) {
		remoteDataSource.join(email, password)
	}

	override suspend fun save(email: String, token: AccountToken) {
		preferencesDataSource.save(email, token.uid, token.token)
	}

	override suspend fun clear() {
		preferencesDataSource.clear()
	}

	override fun fetchToken(email: String, password: String): Flow<AccountToken> = flow { emit(remoteDataSource.login(email, password)) }
}
