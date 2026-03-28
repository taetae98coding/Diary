package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TagDao : RoomDao<TagLocalEntity> {
    @Query("SELECT * FROM Tag WHERE id = :tagId")
    fun get(tagId: Uuid): Flow<TagLocalEntity?>

    @Query(
        """
        UPDATE Tag
        SET
            title = :title,
            description = :description,
            color = :color,
            updatedAt = :updatedAt
        WHERE id = :tagId
        """,
    )
    suspend fun updateDetail(
        tagId: Uuid,
        title: String,
        description: String,
        color: Int,
        updatedAt: Long,
    )

    @Query("UPDATE Tag SET isFinished = :isFinished, updatedAt = :updatedAt WHERE id = :tagId")
    suspend fun updateFinish(
        tagId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    @Query("UPDATE Tag SET isDeleted = :isDeleted, updatedAt = :updatedAt WHERE id = :tagId")
    suspend fun updateDelete(
        tagId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )
}
