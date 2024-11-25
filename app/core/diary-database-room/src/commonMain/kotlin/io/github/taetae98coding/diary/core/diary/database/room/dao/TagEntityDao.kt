package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
internal abstract class TagEntityDao : EntityDao<TagEntity>() {
	@Query(
		"""
		UPDATE TagEntity
		SET
		title = :title,
		description = :description,
		color = :color,
		updateAt = :updateAt
		WHERE id = :tagId
	""",
	)
	abstract suspend fun update(
		tagId: String,
		title: String,
		description: String,
		color: Int,
		updateAt: Instant,
	)

	@Query(
		"""
		UPDATE TagEntity
		SET
		isFinish = :isFinish,
		updateAt = :updateAt
		WHERE id = :tagId
	""",
	)
	abstract suspend fun updateFinish(tagId: String, isFinish: Boolean, updateAt: Instant)

	@Query(
		"""
		UPDATE TagEntity
		SET
		isDelete = :isDelete,
		updateAt = :updateAt
		WHERE id = :tagId
	""",
	)
	abstract suspend fun updateDelete(tagId: String, isDelete: Boolean, updateAt: Instant)

	@Query(
		"""
		SELECT *
		FROM TagEntity
		WHERE isDelete = 0
		AND isFinish = 0
		AND (owner = :owner OR (owner IS NULL AND :owner IS NULL))
		ORDER BY title
	""",
	)
	abstract fun page(owner: String?): Flow<List<TagEntity>>

	@Query(
		"""
		SELECT *
		FROM TagEntity
		WHERE id = :tagId
		AND (:filterNotDelete = 0 OR isDelete = 0)
	""",
	)
	abstract fun find(tagId: String, filterNotDelete: Boolean): Flow<TagEntity?>

	@Query(
		"""
		SELECT *
		FROM TagEntity
		WHERE id IN (:tagIds)
		AND (:filterNotDelete = 0 OR isDelete = 0)
	""",
	)
	abstract fun findByIds(tagIds: Set<String>, filterNotDelete: Boolean): Flow<List<TagEntity>>

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
		ORDER BY title
	""",
	)
	abstract fun findMemoByTagId(tagId: String): Flow<List<MemoEntity>>

	@Query("SELECT MAX(serverUpdateAt) FROM TagEntity WHERE owner = :owner")
	abstract fun getLastUpdateAt(owner: String?): Flow<Instant?>
}
