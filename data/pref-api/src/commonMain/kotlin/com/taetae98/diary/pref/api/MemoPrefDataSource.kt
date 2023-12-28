package com.taetae98.diary.pref.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoPrefDataSource {
    public suspend fun setFetchAt(fetchAt: Instant)
    public fun getFetchAt(): Flow<Instant?>
}
