package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toEntity
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoRoomDao(
    private val clock: Clock,
    private val database: DiaryDatabase,
) : MemoDao {
    override suspend fun upsert(memo: MemoDto) {
        database.memo().upsert(memo.toEntity())
    }

    override suspend fun upsert(memoList: List<MemoDto>) {
        database.memo().upsert(memoList.map(MemoDto::toEntity))
    }

    override suspend fun update(memoId: String, detail: MemoDetail) {
        database.memo()
            .update(
                memoId = memoId,
                title = detail.title,
                description = detail.description,
                start = detail.start,
                endInclusive = detail.endInclusive,
                color = detail.color,
                updateAt = clock.now(),
            )
    }

    override suspend fun updateFinish(memoId: String, isFinish: Boolean) {
        database.memo().updateFinish(memoId, isFinish, clock.now())
    }

    override suspend fun updateDelete(memoId: String, isDelete: Boolean) {
        database.memo().updateDelete(memoId, isDelete, clock.now())
    }

    override fun find(memoId: String): Flow<MemoDto?> {
        return database.memo().find(memoId)
            .mapLatest { it?.toDto() }
    }

    override fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>> {
        return database.memo().findByDateRange(owner, dateRange.start, dateRange.endInclusive)
            .mapCollectionLatest(MemoEntity::toDto)
    }

    override fun getLastServerUpdateAt(owner: String?): Flow<Instant?> {
        return database.memo().getLastUpdateAt(owner)
    }
}
