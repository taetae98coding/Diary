package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoFilterTagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountCalendarMemoFilterTagRepositoryImpl(
    @param:Provided
    private val accountCalendarMemoFilterTagLocalDataSource: AccountCalendarMemoFilterTagLocalDataSource,
) : AccountCalendarMemoFilterTagRepository {
    override fun get(accountId: Uuid): Flow<Set<Uuid>> {
        return accountCalendarMemoFilterTagLocalDataSource.get(accountId).map { it.toSet() }
    }
}
