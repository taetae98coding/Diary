package com.taetae98.diary.data.local.impl.memo.tag

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.MemoTagEntity
import com.taetae98.diary.data.local.impl.memo.mapToMemoDto
import com.taetae98.diary.library.kotlin.ext.mapCollectionLatest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoTagLocalDataSourceImpl(
    private val database: DiaryDatabase,
    @Named(CoroutinesModule.DATABASE)
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

    override suspend fun upsert(memoTag: MemoTagDto) {
        database.memoTagEntityQueries.upsert(memoTag.toEntity())
    }

    override suspend fun upsert(memoTag: List<MemoTagDto>) {
        database.transaction { memoTag.forEach { upsert(it) } }
    }

    override suspend fun afterAt(ownerId: String, updateAt: Instant?, limit: Long): List<MemoDto> {
        return database.memoTagEntityQueries.afterAt(
            ownerId = ownerId,
            limit = limit,
            updateAt = updateAt ?: Instant.DISTANT_PAST,
            mapper = ::mapToMemoDto,
        ).awaitAsList()
    }

    override fun findByMemoId(memoId: String): Flow<List<MemoTagDto>> {
        return database.memoTagEntityQueries.findByMemoId(memoId)
            .asFlow()
            .mapToList(dispatcher)
            .mapCollectionLatest(MemoTagEntity::toDto)
    }
}
