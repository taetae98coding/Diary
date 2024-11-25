package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoTagEntityDao : EntityDao<MemoTagEntity>() {
	@Query(
		"""
		SELECT tagId
		FROM MemoTagEntity
		WHERE memoId = :memoId
	""",
	)
	abstract fun findTagIdsByMemoId(memoId: String): Flow<List<String>>
}
