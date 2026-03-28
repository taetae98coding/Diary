package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MemoTagDao : RoomDao<MemoTagLocalEntity> {
    @Query(
        """
        SELECT Tag.*
        FROM Tag
        INNER JOIN MemoTag ON MemoTag.memoId = :memoId AND MemoTag.tagId = Tag.id AND MemoTag.isMemoTag = 1
        WHERE Tag.isDeleted = 0
        ORDER BY Tag.title
        """,
    )
    fun get(memoId: Uuid): Flow<List<TagLocalEntity>>
}
