package io.github.taetae98coding.diary.core.database.api.datasource

import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountCalendarRoutineLocalDataSource {
    public fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<RoutineLocalEntity>>
}
