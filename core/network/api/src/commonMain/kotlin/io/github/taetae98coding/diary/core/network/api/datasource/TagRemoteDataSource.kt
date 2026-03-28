package io.github.taetae98coding.diary.core.network.api.datasource

import io.github.taetae98coding.diary.core.network.api.entity.TagRemoteEntity

public interface TagRemoteDataSource {
    public suspend fun push(tagList: List<TagRemoteEntity>)

    public suspend fun pull(updatedAt: Long): List<TagRemoteEntity>
}
