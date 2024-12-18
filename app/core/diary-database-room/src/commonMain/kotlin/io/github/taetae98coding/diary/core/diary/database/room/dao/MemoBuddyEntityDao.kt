package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBuddyGroupEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoBuddyEntityDao : EntityDao<MemoBuddyGroupEntity>() {
	@Transaction
	open suspend fun upsert(memoIds: Set<String>, groupId: String) {
		memoIds.forEach { memoId ->
			upsert(MemoBuddyGroupEntity(memoId, groupId))
		}
	}

	@Query(
		"""
		SELECT EXISTS(
			SELECT *
			FROM MemoBuddyGroupEntity
			WHERE memoId = :memoId
		)
	""",
	)
	abstract fun isBuddyGroupMemo(memoId: String): Flow<Boolean>
}
