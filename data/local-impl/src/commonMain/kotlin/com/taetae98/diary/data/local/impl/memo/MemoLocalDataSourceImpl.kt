package com.taetae98.diary.data.local.impl.memo

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoStateDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.di.DatabaseModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
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

    override suspend fun fetch(memo: List<MemoDto>) {
        database.transaction {
            memo.forEach {
                when (it.state) {
                    MemoStateDto.DELETE -> delete(it.id)
                    else -> upsert(it)
                }
            }
        }
    }

    override suspend fun complete(id: String) {
        database.memoEntityQueries.complete(id)
    }

    override suspend fun incomplete(id: String) {
        database.memoEntityQueries.incomplete(id)
    }

    override suspend fun delete(id: String) {
        database.memoEntityQueries.delete(id)
    }

    override fun find(id: String): Flow<MemoDto?> {
        return database.memoEntityQueries.findById(id, ::mapToMemoDto)
            .asFlow()
            .mapToOneOrNull(dispatcher)
    }

    override fun find(ownerId: String?, dateRange: ClosedRange<LocalDate>, ): Flow<List<MemoDto>> {
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
}