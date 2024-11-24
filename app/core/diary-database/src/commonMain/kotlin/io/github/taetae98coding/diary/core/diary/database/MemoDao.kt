package io.github.taetae98coding.diary.core.diary.database

import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

public interface MemoDao {
	public suspend fun upsert(dto: MemoAndTagIds)

	public suspend fun upsert(memoList: List<MemoAndTagIds>)

	public suspend fun update(memoId: String, detail: MemoDetail)

	public suspend fun updatePrimaryTag(memoId: String, tagId: String?)

	public suspend fun updateFinish(memoId: String, isFinish: Boolean)

	public suspend fun updateDelete(memoId: String, isDelete: Boolean)

	public fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>, tagFilter: Set<String>): Flow<List<MemoDto>>

	public fun find(memoId: String, filterNotDelete: Boolean): Flow<MemoDto?>

	public fun findMemoAndTagIdsByIds(memoIds: Set<String>, filterNotDelete: Boolean): Flow<List<MemoAndTagIds>>

	public fun getLastServerUpdateAt(owner: String?): Flow<Instant?>
}
