package com.taetae98.diary.data.local.impl.memo.tag

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagLocalDataSourceImpl(
    private val database: DiaryDatabase,
) : MemoTagLocalDataSource {
    override suspend fun exists(memoTag: MemoTagDto): Boolean {
        val query = database.memoTagEntityQueries.exists(
            memoId = memoTag.memoId,
            tagId = memoTag.tagId,
        )

        return query.awaitAsOneOrNull() ?: false
    }

    override suspend fun delete(memoTag: MemoTagDto) {
        database.memoTagEntityQueries.delete(
            memoId = memoTag.memoId,
            tagId = memoTag.tagId,
        )
    }

    override suspend fun upsert(memoTag: MemoTagDto) {
        database.memoTagEntityQueries.upsert(memoTag.toEntity())
    }
}