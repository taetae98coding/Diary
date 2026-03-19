package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
internal interface MemoDao : RoomDao<MemoLocalEntity> {
    @Query("SELECT * FROM Memo WHERE id = :memoId")
    fun get(memoId: Uuid): Flow<MemoLocalEntity?>

    @Query(
        """
        UPDATE Memo
        SET
            title = :title,
            description = :description,
            isAllDay = :isAllDay,
            start = :start,
            endInclusive = :endInclusive,
            color = :color,
            updatedAt = :updatedAt
        WHERE id = :memoId
        """,
    )
    suspend fun updateDetail(
        memoId: Uuid,
        title: String,
        description: String,
        isAllDay: Boolean,
        start: LocalDateTime?,
        endInclusive: LocalDateTime?,
        color: Int,
        updatedAt: Long,
    )

    @Query("UPDATE Memo SET isFinished = :isFinished, updatedAt = :updatedAt WHERE id = :memoId")
    suspend fun updateFinish(
        memoId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    @Query("UPDATE Memo SET isDeleted = :isDeleted, updatedAt = :updatedAt WHERE id = :memoId")
    suspend fun updateDelete(
        memoId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )
}
