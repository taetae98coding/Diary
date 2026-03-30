package io.github.taetae98coding.diary.core.database.impl.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.datasource.AccountMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class AccountMemoLocalDataSourceImpl(private val database: DiaryDatabase) : AccountMemoLocalDataSource {
    override suspend fun upsert(entity: AccountMemoLocalEntity) {
        database.accountMemoDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<AccountMemoLocalEntity>) {
        database.accountMemoDao().upsert(entities)
    }

    override fun pageByTag(
        accountId: Uuid,
        tagId: Uuid,
    ): PagingSource<Int, MemoLocalEntity> {
        return database.accountMemoDao().pageByTag(accountId, tagId)
    }
}
