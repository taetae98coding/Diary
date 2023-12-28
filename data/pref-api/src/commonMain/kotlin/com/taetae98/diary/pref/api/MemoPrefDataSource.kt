package com.taetae98.diary.pref.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoPrefDataSource {
    public suspend fun setFetchedUpdateAt(updateAt: Instant)
    public fun getFetchedUpdateAt(): Flow<Instant?>
}
