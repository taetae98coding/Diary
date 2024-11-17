package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoTagEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.library.room.dao.EntityDao
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoTagEntityDao : EntityDao<MemoTagEntity>() {
    @Query("""
        SELECT *
        FROM TagEntity
        WHERE id IN (
            SELECT tagId
            FROM MemoTagEntity
            WHERE memoId = :memoId
        )
        AND isDelete = 0
        ORDER BY title
    """)
    abstract fun findTagByMemoId(memoId: String): Flow<List<TagEntity>>
}
