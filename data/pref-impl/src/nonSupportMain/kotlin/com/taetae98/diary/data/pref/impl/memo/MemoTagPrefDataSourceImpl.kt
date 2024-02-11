package com.taetae98.diary.data.pref.impl.memo

import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant

internal class MemoTagPrefDataSourceImpl : MemoTagPrefDataSource {
    override suspend fun setFetchedUpdateAt(uid: String, updateAt: Instant) = Unit
    override fun getFetchedUpdateAt(uid: String): Flow<Instant?> {
        return flowOf(Instant.DISTANT_FUTURE)
    }
}