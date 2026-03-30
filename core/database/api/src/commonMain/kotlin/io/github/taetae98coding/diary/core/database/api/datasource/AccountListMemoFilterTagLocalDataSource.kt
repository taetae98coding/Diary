package io.github.taetae98coding.diary.core.database.api.datasource

import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountListMemoFilterTagLocalDataSource {
    public fun get(accountId: Uuid): Flow<List<Uuid>>
}
