package com.taetae98.diary.data.local.impl.tag

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagInMemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.TagInMemoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TagInMemoLocalDataSource(
    private val database: DiaryDatabase,
    @Named(CoroutinesModule.DATABASE)
    private val dispatcher: CoroutineDispatcher,
) : TagInMemoLocalDataSource {
    override fun find(ownerId: String?): Flow<List<TagDto>> {
        return database.tagInMemoEntityQueries.find(ownerId = ownerId, mapper = ::mapToTagDto)
            .asFlow()
            .mapToList(dispatcher)
    }

    override suspend fun upsert(tagId: String) {
        database.tagInMemoEntityQueries.upsert(TagInMemoEntity(tagId))
    }

    override suspend fun delete(tagId: String) {
        database.tagInMemoEntityQueries.delete(tagId)
    }
}
