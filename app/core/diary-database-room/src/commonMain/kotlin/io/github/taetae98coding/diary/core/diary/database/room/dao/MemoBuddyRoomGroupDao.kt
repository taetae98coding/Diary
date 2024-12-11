package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoBuddyGroupDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoBuddyRoomGroupDao(
	private val database: DiaryDatabase,
) : MemoBuddyGroupDao {
	override suspend fun upsert(memoIds: Set<String>, groupId: String) {
		database.memoBuddyGroup().upsert(memoIds, groupId)
	}

	override fun isBuddyGroupMemo(memoId: String): Flow<Boolean> = database.memoBuddyGroup().isBuddyGroupMemo(memoId)
}
