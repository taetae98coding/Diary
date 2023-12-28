package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.pref.api.MemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoPrefDataSourceImpl : MemoPrefDataSource {
    override suspend fun setFetchedUpdateAt(updateAt: Instant) {

    }

    override fun getFetchedUpdateAt(): Flow<Instant?> {
        return flowOf(null)
    }
}