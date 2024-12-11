package io.github.taetae98coding.diary.domain.memo.repository

import kotlinx.coroutines.flow.Flow

public interface MemoBuddyRepository {
	public fun findBuddyIdByMemoId(memoId: String): Flow<List<String>>
}
