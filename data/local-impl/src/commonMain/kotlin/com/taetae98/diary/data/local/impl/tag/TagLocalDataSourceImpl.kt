package com.taetae98.diary.data.local.impl.tag

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TagLocalDataSourceImpl(
    private val database: DiaryDatabase,
    @Named(CoroutinesModule.DATABASE)
    private val dispatcher: CoroutineDispatcher,
) : TagLocalDataSource {
    override suspend fun upsert(tag: TagDto) {
        database.tagEntityQueries.upsert(tag.toEntity())
    }

    override suspend fun delete(id: String) {
        database.tagEntityQueries.delete(id)
    }

    override fun page(ownerId: String?): PagingSource<Int, TagDto> {
        val queries = database.tagEntityQueries

        return QueryPagingSource(
            countQuery = queries.count(ownerId = ownerId),
            transacter = queries,
            context = dispatcher,
            queryProvider = { limit, offset ->
                queries.page(
                    ownerId = ownerId,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToTagDto,
                )
            }
        )
    }

    override fun find(id: String): Flow<TagDto?> {
        return database.tagEntityQueries.findById(id, ::mapToTagDto)
            .asFlow()
            .mapToOneOrNull(dispatcher)
    }
}
