package com.taetae98.diary.data.pref.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

public interface MemoPrefDataSource {
    public suspend fun setFetchedUpdateAt(uid: String, updateAt: Instant)
    public fun getFetchedUpdateAt(uid: String): Flow<Instant?>
}
