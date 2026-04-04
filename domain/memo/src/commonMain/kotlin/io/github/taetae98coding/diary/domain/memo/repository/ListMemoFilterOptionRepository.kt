package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import kotlinx.coroutines.flow.Flow

public interface ListMemoFilterOptionRepository {
    public fun get(): Flow<ListMemoFilterOption>

    public suspend fun updateTagPresence(presence: FilterPresence)

    public suspend fun updateDatePresence(presence: FilterPresence)
}
