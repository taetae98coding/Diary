package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountCalendarRoutineLocalDataSourceImpl(private val database: DiaryDatabase) : AccountCalendarRoutineLocalDataSource {
    override fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<RoutineLocalEntity>> {
        return database.accountCalendarRoutineDao().get(accountId = accountId, year = year)
    }
}
