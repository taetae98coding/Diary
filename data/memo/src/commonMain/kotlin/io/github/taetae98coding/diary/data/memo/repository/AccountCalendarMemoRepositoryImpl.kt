package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarMemoLocalDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.memo.CalendarMemo
import io.github.taetae98coding.diary.domain.memo.repository.AccountCalendarMemoRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class AccountCalendarMemoRepositoryImpl(private val accountCalendarMemoLocalDataSource: AccountCalendarMemoLocalDataSource) : AccountCalendarMemoRepository {
    override fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemo>> {
        return accountCalendarMemoLocalDataSource
            .get(accountId, year)
            .map { entities -> entities.map { it.toDomain() } }
    }
}
