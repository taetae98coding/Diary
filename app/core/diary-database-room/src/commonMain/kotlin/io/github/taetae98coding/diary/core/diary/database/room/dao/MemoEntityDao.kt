package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoAccountEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Dao
internal abstract class MemoEntityDao : EntityDao<MemoEntity>() {
	@Transaction
	open suspend fun upsert(owner: String, memoList: List<MemoEntity>, tagIdsList: List<Set<String>>) {
		memoList.forEachIndexed { index, memo ->
			upsert(owner, memo, tagIdsList[index])
		}
	}

	@Transaction
	open suspend fun upsert(owner: String, memo: MemoEntity, tagIds: Set<String>) {
		upsert(memo)
		upsertMemoAccount(MemoAccountEntity(memoId = memo.id, owner = owner))
		deleteMemoTag(memo.id)
		tagIds.forEach {
			upsertMemoTag(MemoTagEntity(memoId = memo.id, tagId = it))
		}
	}

	@Upsert
	abstract suspend fun upsertMemoAccount(entity: MemoAccountEntity)

	@Query("DELETE FROM MemoTagEntity WHERE memoId = :memoId")
	abstract suspend fun deleteMemoTag(memoId: String)

	@Upsert
	abstract suspend fun upsertMemoTag(entity: MemoTagEntity)

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
	""",
	)
	abstract fun getById(memoId: String): Flow<MemoEntity?>

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		LEFT OUTER JOIN MemoTagEntity
		ON MemoEntity.id = MemoTagEntity.memoId
		WHERE MemoEntity.id IN (:memoIds)
	""",
	)
	abstract fun getMemoAndTagIdsByIds(memoIds: Set<String>): Flow<Map<MemoEntity, List<MemoTagEntity>>>

	@Query(
		"""
		SELECT *
		FROM MemoEntity
		WHERE isDelete = 0
		AND start IS NOT NULL
		AND endInclusive IS NOT NULL
		AND endInclusive >= :start AND start <= :endInclusive
		AND (:hasTagFilter = 0 OR id IN (SELECT memoId FROM MemoTagEntity WHERE tagId in (:tagFilter)))
		AND id IN (SELECT memoId FROM MemoAccountEntity WHERE owner = :owner)
		ORDER BY start, endInclusive, title
	""",
	)
	abstract fun findByDateRange(owner: String, start: LocalDate, endInclusive: LocalDate, hasTagFilter: Boolean, tagFilter: Set<String>): Flow<List<MemoEntity>>

	@Query(
		"""
			SELECT MAX(serverUpdateAt)
			FROM MemoEntity
			WHERE id IN (SELECT memoId FROM MemoAccountEntity WHERE owner = :owner)
	""",
	)
	abstract fun getLastUpdateAt(owner: String?): Flow<Instant?>
}
