package io.github.taetae98coding.diary.domain.memo.repository

import kotlinx.coroutines.flow.Flow

public interface MemoBuddyGroupRepository {
	public fun isBuddyGroupMemo(memoId: String): Flow<Boolean>
}
