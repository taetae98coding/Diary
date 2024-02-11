package com.taetae98.diary.data.local.impl.memo.tag

import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.MemoTagEntity
import com.taetae98.diary.data.local.impl.di.DatabaseModule
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoTagLocalDataSourceImpl(
    private val database: DiaryDatabase,
    @Named(DatabaseModule.DATABASE_DISPATCHER)
    private val dispatcher: CoroutineDispatcher,
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

    override suspend fun insert(memoTag: MemoTagDto) {
        val entity = MemoTagEntity(
            memoId = memoTag.memoId,
            tagId = memoTag.tagId,
        )

        database.memoTagEntityQueries.insert(entity)
    }

    override suspend fun insert(memoTag: List<MemoTagDto>) {
        database.transaction { memoTag.forEach { insert(it) } }
    }

    override fun findByMemoId(memoId: String): Flow<List<MemoTagDto>> {
        return database.memoTagEntityQueries.findByMemoId(memoId)
            .asFlow()
            .mapToList(dispatcher)
            .mapCollectionLatest(MemoTagEntity::toDto)
    }
}
