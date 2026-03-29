package io.github.taetae98coding.diary.core.database.api.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import kotlin.uuid.Uuid

public interface AccountTagLocalDataSource {
    public suspend fun upsert(entity: AccountTagLocalEntity)
    public suspend fun upsert(entities: Collection<AccountTagLocalEntity>)

    public fun page(accountId: Uuid): PagingSource<Int, TagLocalEntity>
}
