package io.github.taetae98coding.diary.domain.buddy.repository

import io.github.taetae98coding.diary.core.model.Buddy
import io.github.taetae98coding.diary.core.model.BuddyGroup
import io.github.taetae98coding.diary.core.model.BuddyGroupAndBuddyIds
import io.github.taetae98coding.diary.core.model.Memo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface BuddyRepository {
	public suspend fun upsert(buddyGroup: BuddyGroupAndBuddyIds)
    public suspend fun upsert(groupId: String, memo: Memo, tagIds: Set<String>)

	public fun findGroupById(id: String): Flow<BuddyGroup?>

	public fun findGroupByUid(uid: String): Flow<List<BuddyGroup>>

    public fun findBuddyIdByGroupId(groupId: String): Flow<List<String>>
	public fun findBuddyByEmail(email: String, uid: String?): Flow<List<Buddy>>

    public fun findMemoByDate(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>>

}
