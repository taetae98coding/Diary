package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagInMemoLocalDataSource
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagInMemoRepository
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class TagInMemoRepositoryImpl(
    private val localDataSource: TagInMemoLocalDataSource,
):TagInMemoRepository{
    override fun find(ownerId: String?): Flow<List<Tag>> {
        return localDataSource.find(ownerId)
            .mapCollectionLatest(TagDto::toDomain)
    }
}