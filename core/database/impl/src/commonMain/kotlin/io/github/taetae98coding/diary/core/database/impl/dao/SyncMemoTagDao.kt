package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.MemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
internal interface SyncMemoTagDao : RoomDao<SyncMemoTagLocalEntity> {
    @Query(
        """
        SELECT MemoTag.*
        FROM MemoTag
        INNER JOIN SyncMemoTag ON SyncMemoTag.memoId = MemoTag.memoId AND SyncMemoTag.tagId = MemoTag.tagId
        INNER JOIN AccountMemo ON AccountMemo.memoId = MemoTag.memoId AND AccountMemo.accountId = :accountId
        WHERE SyncMemoTag.state = :state
        LIMIT 100
        """,
    )
    suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoTagLocalEntity>

    @Query(
        """
        SELECT COALESCE(MAX(MemoTag.updatedAt), 0)
        FROM MemoTag
        INNER JOIN SyncMemoTag ON SyncMemoTag.memoId = MemoTag.memoId AND SyncMemoTag.tagId = MemoTag.tagId
        INNER JOIN AccountMemo ON AccountMemo.memoId = MemoTag.memoId AND AccountMemo.accountId = :accountId
        WHERE SyncMemoTag.state = :state
        """,
    )
    suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
