package io.github.taetae98coding.diary.core.database.api.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import kotlin.uuid.Uuid

public interface AccountMemoLocalDataSource {
    public suspend fun upsert(entity: AccountMemoLocalEntity)
    public suspend fun upsert(entities: Collection<AccountMemoLocalEntity>)

    public fun pageByTag(
        accountId: Uuid,
        tagId: Uuid,
    ): PagingSource<Int, MemoLocalEntity>
}
