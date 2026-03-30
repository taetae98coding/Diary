package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoFilterTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
public class AccountCalendarMemoFilterTagRepositoryImpl(private val accountCalendarMemoFilterTagLocalDataSource: AccountCalendarMemoFilterTagLocalDataSource) : AccountCalendarMemoFilterTagRepository {
    override fun get(accountId: Uuid): Flow<List<Uuid>> {
        return accountCalendarMemoFilterTagLocalDataSource.get(accountId)
    }
}
