package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Dao
internal abstract class MemoEntityDao : EntityDao<MemoEntity>() {
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
    abstract fun find(memoId: String): Flow<MemoEntity?>

    @Query(
        """
        SELECT *
        FROM MemoEntity
        WHERE isDelete = 0
        AND (owner = :owner OR (owner IS NULL AND :owner IS NULL))
        AND start IS NOT NULL
        AND endInclusive IS NOT NULL
        AND endInclusive >= :start AND start <= :endInclusive
    """,
    )
    abstract fun findByDateRange(owner: String?, start: LocalDate, endInclusive: LocalDate): Flow<List<MemoEntity>>

    @Query("SELECT MAX(serverUpdateAt) FROM MemoEntity WHERE owner = :owner")
    abstract fun getLastUpdateAt(owner: String?): Flow<Instant?>
}
