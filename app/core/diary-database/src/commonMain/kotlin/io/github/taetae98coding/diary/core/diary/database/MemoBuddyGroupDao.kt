package io.github.taetae98coding.diary.core.diary.database

import kotlinx.coroutines.flow.Flow

public interface MemoBuddyGroupDao {
	public suspend fun upsert(memoIds: Set<String>, groupId: String)

	public fun isBuddyGroupMemo(memoId: String): Flow<Boolean>
}
