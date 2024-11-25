package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toEntity
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Dao
internal abstract class MemoEntityDao : EntityDao<MemoEntity>() {
	@Transaction
	open suspend fun upsertMemoAndTagIds(memo: MemoAndTagIds) {
		upsert(memo.memo.toEntity())
		deleteMemoTag(memo.memo.id)
		memo.tagIds.forEach {
			upsertMemoTag(MemoTagEntity(memoId = memo.memo.id, tagId = it))
		}
	}

	@Transaction
	open suspend fun upsertMemoAndTagIds(memoList: List<MemoAndTagIds>) {
		memoList.forEach { upsertMemoAndTagIds(it) }
	}

	@Upsert
	abstract suspend fun upsertMemoTag(entity: MemoTagEntity)

	@Query("DELETE FROM MemoTagEntity WHERE memoId = :memoId")
	abstract suspend fun deleteMemoTag(memoId: String)

	@Query(
		"""
		UPDATE MemoEntity
		SET
		title = :title,
		description = :description,
		start = :start,
		endInclusive = :endInclusive,
		color = :color,
		updateAt = :updateAt
		WHERE id = :memoId
	""",
	)
	abstract suspend fun update(
		memoId: String,
		title: String,
		description: String,
		start: LocalDate?,
		endInclusive: LocalDate?,
		color: Int,
		updateAt: Instant,
	)

	@Query(
		"""
		UPDATE MemoEntity
		SET
		primaryTag = :primaryTag,
		updateAt = :updateAt
		WHERE id = :memoId
	""",
	)
	abstract suspend fun updatePrimaryTag(memoId: String, primaryTag: String?, updateAt: Instant)

	@Query(
		"""
		UPDATE MemoEntity
		SET
		isFinish = :isFinish,
		updateAt = :updateAt
		WHERE id = :memoId
	""",
	)
	abstract suspend fun updateFinish(memoId: String, isFinish: Boolean, updateAt: Instant)

	@Query(
		"""
		UPDATE MemoEntity
		SET
		isDelete = :isDelete,
		updateAt = :updateAt
		WHERE id = :memoId
	""",
	)
	abstract suspend fun updateDelete(memoId: String, isDelete: Boolean, updateAt: Instant)

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		WHERE id = :memoId
		AND (:filterNotDelete = 0 OR isDelete = 0)
	""",
	)
	abstract fun find(memoId: String, filterNotDelete: Boolean): Flow<MemoEntity?>

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		LEFT OUTER JOIN MemoTagEntity
		ON MemoEntity.id = MemoTagEntity.memoId
		WHERE MemoEntity.id IN (:memoIds)
		AND (:filterNotDelete = 0 OR isDelete = 0)
	""",
	)
	abstract fun findMemoAndTagIdsByIds(memoIds: Set<String>, filterNotDelete: Boolean): Flow<Map<MemoEntity, List<MemoTagEntity>>>

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		WHERE isDelete = 0
		AND (owner = :owner OR (owner IS NULL AND :owner IS NULL))
		AND start IS NOT NULL
		AND endInclusive IS NOT NULL
		AND endInclusive >= :start AND start <= :endInclusive
		AND (:hasTagFilter = 0 OR id IN (SELECT memoId FROM MemoTagEntity WHERE tagId in (:tagFilter)))
	""",
	)
	abstract fun findByDateRange(owner: String?, start: LocalDate, endInclusive: LocalDate, hasTagFilter: Boolean, tagFilter: Set<String>): Flow<List<MemoEntity>>

	@Query("SELECT MAX(serverUpdateAt) FROM MemoEntity WHERE owner = :owner")
	abstract fun getLastUpdateAt(owner: String?): Flow<Instant?>
}
