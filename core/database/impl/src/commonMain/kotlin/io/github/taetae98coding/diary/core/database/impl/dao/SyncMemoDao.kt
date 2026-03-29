package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
internal interface SyncMemoDao : RoomDao<SyncMemoLocalEntity> {
    @Query(
        """
        SELECT Memo.*
        FROM Memo
        INNER JOIN SyncMemo ON SyncMemo.memoId = Memo.id
        INNER JOIN AccountMemo ON AccountMemo.memoId = Memo.id AND AccountMemo.accountId = :accountId
        WHERE SyncMemo.state = :state
        LIMIT 100
        """,
    )
    suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<MemoLocalEntity>

    @Query(
        """
        SELECT COALESCE(MAX(Memo.updatedAt), 0)
        FROM Memo
        INNER JOIN SyncMemo ON SyncMemo.memoId = Memo.id
        INNER JOIN AccountMemo ON AccountMemo.memoId = Memo.id AND AccountMemo.accountId = :accountId
        WHERE SyncMemo.state = :state
        """,
    )
    suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
