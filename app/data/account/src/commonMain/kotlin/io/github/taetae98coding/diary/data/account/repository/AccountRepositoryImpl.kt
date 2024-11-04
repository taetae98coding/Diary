package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import io.github.taetae98coding.diary.core.diary.service.account.AccountService
import io.github.taetae98coding.diary.core.model.account.AccountToken
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class AccountRepositoryImpl(
    private val preferencesDataSource: AccountPreferences,
    private val remoteDataSource: AccountService,
) : AccountRepository {
    override suspend fun join(email: String, password: String) {
        remoteDataSource.join(email, password)
    }

    override suspend fun save(email: String, token: AccountToken) {
        preferencesDataSource.save(email, token.uid, token.token)
    }

    override suspend fun clear() {
        preferencesDataSource.clear()
    }

    override fun getEmail(): Flow<String?> {
        return preferencesDataSource.getEmail()
    }

    override fun getUid(): Flow<String?> {
        return preferencesDataSource.getUid()
    }

    override fun fetchToken(email: String, password: String): Flow<AccountToken> {
        return flow { emit(remoteDataSource.login(email, password)) }
            .mapLatest {
                AccountToken(
                    uid = it.uid,
                    token = it.token,
                )
            }
    }
}
