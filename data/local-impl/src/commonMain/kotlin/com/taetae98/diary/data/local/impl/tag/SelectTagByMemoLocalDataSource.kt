package com.taetae98.diary.data.local.impl.tag

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.SelectTagByMemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.SelectTagByMemoEntity
import com.taetae98.diary.data.local.impl.memo.mapToMemoDto
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

    override fun page(ownerId: String?, includeNoTag: Boolean): PagingSource<Int, MemoDto> {
        val queries = database.selectTagByMemoEntityQueries

        return QueryPagingSource(
            countQuery = queries.count(ownerId = ownerId),
            transacter = queries,
            context = dispatcher,
            queryProvider = { limit, offset ->
                queries.page(
                    ownerId = ownerId,
                    includeNoTag = if (includeNoTag) 1L else 0L,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToMemoDto,
                )
            },
        )
    }

    override suspend fun upsert(tagId: String) {
        database.selectTagByMemoEntityQueries.upsert(SelectTagByMemoEntity(tagId))
    }

    override suspend fun delete(tagId: String) {
        database.selectTagByMemoEntityQueries.delete(tagId)
    }
}
