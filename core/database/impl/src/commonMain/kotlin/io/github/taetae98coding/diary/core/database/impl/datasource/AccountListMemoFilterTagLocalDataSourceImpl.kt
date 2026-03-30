package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.AccountListMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountListMemoFilterTagLocalDataSourceImpl(private val database: DiaryDatabase) : AccountListMemoFilterTagLocalDataSource {
    override fun get(accountId: Uuid): Flow<List<Uuid>> {
        return database.accountListMemoFilterTagDao().get(accountId)
    }
}
