package com.taetae98.diary.local.impl.memo

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.local.impl.di.DatabaseModule
import kotlinx.coroutines.CoroutineDispatcher
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

    override fun page(): PagingSource<Int, MemoDto> {
        val queries = database.memoEntityQueries

        return QueryPagingSource(
            countQuery = queries.count(),
            transacter = queries,
            context = dispatcher,
            queryProvider = { limit, offset ->
                queries.page(limit, offset, ::MemoDto)
            },
        )
    }
}