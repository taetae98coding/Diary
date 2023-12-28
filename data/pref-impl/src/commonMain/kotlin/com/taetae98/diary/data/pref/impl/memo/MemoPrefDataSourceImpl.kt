package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.pref.api.MemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoPrefDataSourceImpl : MemoPrefDataSource {
    override suspend fun setSyncAt(migrateAt: Instant) {
        TODO("Not yet implemented")
    }

    override fun getSyncAt(): Flow<Instant> {
        TODO("Not yet implemented")
    }
}