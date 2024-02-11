package com.taetae98.diary.data.local.impl.memo

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.di.DatabaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoLocalDataSourceImpl(
    private val database: DiaryDatabase,
    @Named(DatabaseModule.DATABASE_DISPATCHER)
    private val dispatcher: CoroutineDispatcher,
) : MemoLocalDataSource {
    override suspend fun upsert(memo: MemoDto) {
        database.memoEntityQueries.upsert(memo.toEntity())
    }

    override suspend fun updateFinish(id: String, isFinished: Boolean) {
        database.memoEntityQueries.updateFinished(id = id, isFinished = isFinished)
    }

    override suspend fun delete(id: String) {
        database.memoEntityQueries.delete(id)
    }

    override fun find(id: String): Flow<MemoDto?> {
        return database.memoEntityQueries.findById(id, ::mapToMemoDto)
            .asFlow()
            .mapToOneOrNull(dispatcher)
    }

    override fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>> {
        return database.memoEntityQueries.findByYearAndMonth(
            start = dateRange.start,
            end = dateRange.endInclusive,
            ownerId = ownerId,
            mapper = ::mapToMemoDto
        ).asFlow().mapToList(dispatcher)
    }

    override fun page(ownerId: String?): PagingSource<Int, MemoDto> {
        val queries = database.memoEntityQueries

        return QueryPagingSource(
            countQuery = queries.count(ownerId = ownerId),
            transacter = queries,
            context = dispatcher,
            queryProvider = { limit, offset ->
                queries.page(
                    ownerId = ownerId,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToMemoDto
                )
            },
        )
    }

    override fun page(ownerId: String?, tagId: String): PagingSource<Int, MemoDto> {
        val queries = database.memoEntityQueries

        return QueryPagingSource(
            countQuery = queries.countByTagId(ownerId = ownerId, tagId = tagId),
            transacter = queries,
            context = dispatcher,
            queryProvider = { limit, offset ->
                queries.pageByTagId(
                    ownerId = ownerId,
                    tagId = tagId,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToMemoDto
                )
            },
        )
    }
}
