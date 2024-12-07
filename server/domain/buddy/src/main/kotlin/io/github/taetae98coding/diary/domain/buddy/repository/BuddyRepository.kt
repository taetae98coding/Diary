package io.github.taetae98coding.diary.domain.buddy.repository

import io.github.taetae98coding.diary.core.model.Buddy
import io.github.taetae98coding.diary.core.model.BuddyGroup
import io.github.taetae98coding.diary.core.model.BuddyGroupAndBuddyIds
import kotlinx.coroutines.flow.Flow

public interface BuddyRepository {
	public suspend fun upsert(buddyGroup: BuddyGroupAndBuddyIds)

	public fun findGroupById(id: String): Flow<BuddyGroup?>

	public fun findGroupByUid(uid: String): Flow<List<BuddyGroup>>

	public fun findByEmail(email: String, uid: String?): Flow<List<Buddy>>
}
