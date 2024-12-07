package io.github.taetae98coding.diary.domain.buddy.repository

import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import kotlinx.coroutines.flow.Flow

public interface BuddyRepository {
	public suspend fun upsert(buddyGroup: BuddyGroup, buddyIds: Set<String>)

	public fun findBuddyGroup(): Flow<List<BuddyGroup>>

	public fun findBuddyByEmail(email: String): Flow<List<Buddy>>

	public suspend fun getNextBuddyGroupId(): String
}
