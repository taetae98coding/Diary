package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.SelectTagByMemoLocalDataSource
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class SelectTagByMemoRepositoryImpl(
    private val localDataSource: SelectTagByMemoLocalDataSource,
) : SelectTagByMemoRepository {
    override fun find(ownerId: String?): Flow<List<Tag>> {
        return localDataSource.find(ownerId)
            .mapCollectionLatest(TagDto::toDomain)
    }

    override suspend fun upsert(tagId: String) {
        localDataSource.upsert(tagId)
    }

    override suspend fun delete(tagId: String) {
        localDataSource.delete(tagId)
    }
}