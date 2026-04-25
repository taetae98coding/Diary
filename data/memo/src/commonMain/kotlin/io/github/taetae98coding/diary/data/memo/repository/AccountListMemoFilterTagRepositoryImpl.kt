package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountListMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoFilterTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountListMemoFilterTagRepositoryImpl(
    @param:Provided
    private val accountListMemoFilterTagLocalDataSource: AccountListMemoFilterTagLocalDataSource,
) : AccountListMemoFilterTagRepository {
    override fun get(accountId: Uuid): Flow<Set<Uuid>> {
        return accountListMemoFilterTagLocalDataSource.get(accountId).map { it.toSet() }
    }
}
