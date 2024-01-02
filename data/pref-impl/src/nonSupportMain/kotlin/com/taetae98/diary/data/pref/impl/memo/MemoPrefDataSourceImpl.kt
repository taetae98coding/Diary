package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant

internal class MemoPrefDataSourceImpl : MemoPrefDataSource {
    override suspend fun setFetchedUpdateAt(uid: String, updateAt: Instant) {

    }

    override fun getFetchedUpdateAt(uid: String): Flow<Instant?> {
        return flowOf(null)
    }
}