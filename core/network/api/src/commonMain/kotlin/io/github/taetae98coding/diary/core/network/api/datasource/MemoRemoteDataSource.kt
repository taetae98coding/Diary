package io.github.taetae98coding.diary.core.network.api.datasource

import io.github.taetae98coding.diary.core.network.api.entity.MemoRemoteEntity

public interface MemoRemoteDataSource {
    public suspend fun push(memoList: List<MemoRemoteEntity>)

    public suspend fun pull(updatedAt: Long): List<MemoRemoteEntity>
}
