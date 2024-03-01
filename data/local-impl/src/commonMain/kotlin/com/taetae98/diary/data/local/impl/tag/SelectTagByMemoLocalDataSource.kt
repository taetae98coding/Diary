package com.taetae98.diary.data.local.impl.tag

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.SelectTagByMemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.SelectTagByMemoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class SelectTagByMemoLocalDataSource(
    private val database: DiaryDatabase,
    @Named(CoroutinesModule.DATABASE)
    private val dispatcher: CoroutineDispatcher,
) : SelectTagByMemoLocalDataSource {
    override fun find(ownerId: String?): Flow<List<TagDto>> {
        return database.selectTagByMemoEntityQueries.find(ownerId = ownerId, mapper = ::mapToTagDto)
            .asFlow()
            .mapToList(dispatcher)
    }

    override suspend fun upsert(tagId: String) {
        database.selectTagByMemoEntityQueries.upsert(SelectTagByMemoEntity(tagId))
    }

    override suspend fun delete(tagId: String) {
        database.selectTagByMemoEntityQueries.delete(tagId)
    }
}
