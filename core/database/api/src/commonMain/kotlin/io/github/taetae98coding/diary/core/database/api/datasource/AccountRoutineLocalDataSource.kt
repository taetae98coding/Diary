package io.github.taetae98coding.diary.core.database.api.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import kotlin.uuid.Uuid

public interface AccountRoutineLocalDataSource {
    public suspend fun upsert(entity: AccountRoutineLocalEntity)

    public suspend fun upsert(entities: Collection<AccountRoutineLocalEntity>)

    public fun page(accountId: Uuid): PagingSource<Int, RoutineLocalEntity>
}
