package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.AccountCalendarMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountCalendarMemoFilterTagLocalDataSourceImpl(private val database: DiaryDatabase) : AccountCalendarMemoFilterTagLocalDataSource {
    override fun get(accountId: Uuid): Flow<List<Uuid>> {
        return database.accountCalendarMemoFilterTagDao().get(accountId)
    }
}
