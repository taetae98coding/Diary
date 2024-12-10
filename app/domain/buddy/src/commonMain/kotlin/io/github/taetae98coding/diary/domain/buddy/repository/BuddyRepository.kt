package io.github.taetae98coding.diary.domain.buddy.repository

import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface BuddyRepository {
	public suspend fun upsert(buddyGroup: BuddyGroup, buddyIds: Set<String>)
    public suspend fun upsert(groupId: String, memo: Memo, tagIds: Set<String>)

    public fun findMemoByDateRange(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>>

	public fun findBuddyGroup(): Flow<List<BuddyGroup>>

	public fun findBuddyByEmail(email: String): Flow<List<Buddy>>

	public suspend fun getNextBuddyGroupId(): String
    public suspend fun getNextMemoId(): String
}
