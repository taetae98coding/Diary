package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountListMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoFilterTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
public class AccountListMemoFilterTagRepositoryImpl(private val accountListMemoFilterTagLocalDataSource: AccountListMemoFilterTagLocalDataSource) : AccountListMemoFilterTagRepository {
    override fun get(accountId: Uuid): Flow<List<Uuid>> {
        return accountListMemoFilterTagLocalDataSource.get(accountId)
    }
}
