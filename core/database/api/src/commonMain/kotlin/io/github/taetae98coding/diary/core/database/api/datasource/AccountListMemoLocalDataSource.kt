package io.github.taetae98coding.diary.core.database.api.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.entity.FilterPresenceLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import kotlin.uuid.Uuid

public interface AccountListMemoLocalDataSource {
    public fun page(
        accountId: Uuid,
        tagPresence: FilterPresenceLocalEntity,
        datePresence: FilterPresenceLocalEntity,
    ): PagingSource<Int, MemoLocalEntity>
}
