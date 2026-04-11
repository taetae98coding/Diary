package io.github.taetae98coding.diary.core.database.api.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountRoutineLocalDataSource {
    public suspend fun upsert(entity: AccountRoutineLocalEntity)
    public suspend fun upsert(entities: Collection<AccountRoutineLocalEntity>)

    public fun getByYear(
        accountId: Uuid,
        year: Int,
    ): Flow<List<RoutineLocalEntity>>

    public fun page(accountId: Uuid): PagingSource<Int, RoutineLocalEntity>
}
