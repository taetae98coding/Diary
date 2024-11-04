package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface MemoRepository {
    public suspend fun upsert(uid: String?, memo: Memo)
    public suspend fun update(uid: String?, memoId: String, detail: MemoDetail)
    public suspend fun updateFinish(uid: String?, memoId: String, isFinish: Boolean)
    public suspend fun updateDelete(uid: String?, memoId: String, isDelete: Boolean)

    public fun find(memoId: String): Flow<Memo?>
    public fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>>
}
