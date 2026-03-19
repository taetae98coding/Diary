package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountCalendarMemoLocalDataSourceImpl(private val database: DiaryDatabase) : AccountCalendarMemoLocalDataSource {
    override fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemoLocalEntity>> {
        return database.accountCalendarMemoDao().get(accountId = accountId, year = year)
    }
}
