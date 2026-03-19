package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoLocalDataSourceImpl(private val database: DiaryDatabase) : AccountMemoLocalDataSource {
    override suspend fun upsert(entity: AccountMemoLocalEntity) {
        database.accountMemoDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<AccountMemoLocalEntity>) {
        database.accountMemoDao().upsert(entities)
    }
}
