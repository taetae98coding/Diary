package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoTagEntityDao : EntityDao<MemoTagEntity>() {
	@Query(
		"""
		SELECT *
		FROM TagEntity
		WHERE id IN (
			SELECT tagId
			FROM MemoTagEntity
			WHERE memoId = :memoId
		)
	""",
	)
	abstract fun findTagByMemoId(memoId: String): Flow<List<TagEntity>>

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		WHERE isDelete = 0
		AND isFinish = 0
		AND id IN (
			SELECT memoId
			FROM MemoTagEntity
			WHERE tagId = :tagId
		)
		ORDER BY start, endInclusive, title
	""",
	)
	abstract fun pageMemoByTagId(tagId: String): Flow<List<MemoEntity>>
}
