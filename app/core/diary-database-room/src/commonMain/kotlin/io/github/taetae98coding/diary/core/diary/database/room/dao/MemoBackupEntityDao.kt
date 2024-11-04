package io.github.taetae98coding.diary.core.diary.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoBackupEntity
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MemoBackupEntityDao : EntityDao<MemoBackupEntity>() {
    @Query("DELETE FROM MemoBackupEntity WHERE memoId IN (:memoIds)")
    abstract suspend fun deleteByMemoIds(memoIds: List<String>)

    @Query("SELECT COUNT(memoId) FROM MemoBackupEntity WHERE uid = :uid")
    abstract fun countByUid(uid: String): Flow<Int>

    @Query("""
        SELECT *
        FROM MemoEntity
        WHERE id IN (SELECT memoId FROM MemoBackupEntity WHERE uid = :uid)
        LIMIT 50
    """)
    abstract fun findByUid(uid: String): Flow<List<MemoEntity>>
}
