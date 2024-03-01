package com.taetae98.diary.data.pref.impl.tag

import com.taetae98.diary.data.pref.api.TagPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant

internal class TagPrefDataSourceImpl : TagPrefDataSource {
    override suspend fun setFetchedUpdateAt(uid: String, updateAt: Instant) = Unit

    override fun getFetchedUpdateAt(uid: String): Flow<Instant?> {
        return flowOf(Instant.DISTANT_FUTURE)
    }
}
