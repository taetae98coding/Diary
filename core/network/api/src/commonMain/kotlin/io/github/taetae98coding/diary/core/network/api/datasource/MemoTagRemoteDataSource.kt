package io.github.taetae98coding.diary.core.network.api.datasource

import io.github.taetae98coding.diary.core.network.api.entity.MemoTagRemoteEntity

public interface MemoTagRemoteDataSource {
    public suspend fun push(memoTagList: List<MemoTagRemoteEntity>)

    public suspend fun pull(updatedAt: Long): List<MemoTagRemoteEntity>
}
