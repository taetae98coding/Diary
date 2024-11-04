package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

public interface MemoDao {
    public suspend fun upsert(memo: MemoDto)
    public suspend fun upsert(memoList: List<MemoDto>)

    public suspend fun update(memoId: String, detail: MemoDetail)
    public suspend fun updateFinish(memoId: String, isFinish: Boolean)
    public suspend fun updateDelete(memoId: String, isDelete: Boolean)

    public fun find(memoId: String): Flow<MemoDto?>
    public fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>>
    public fun getLastServerUpdateAt(owner: String?): Flow<Instant?>
}
