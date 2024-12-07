package io.github.taetae98coding.diary.domain.memo.repository

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface MemoRepository {
	public suspend fun upsert(memo: Memo, tagIds: Set<String>)

	public suspend fun update(memoId: String, detail: MemoDetail)

	public suspend fun updatePrimaryTag(memoId: String, tagId: String?)

	public suspend fun updateFinish(memoId: String, isFinish: Boolean)

	public suspend fun updateDelete(memoId: String, isDelete: Boolean)

	public fun getById(memoId: String): Flow<Memo?>

	public fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>, tagFilter: Set<String>): Flow<List<Memo>>

	public suspend fun getNextMemoId(): String
}
