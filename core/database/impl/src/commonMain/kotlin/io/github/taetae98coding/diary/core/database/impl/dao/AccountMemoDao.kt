package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.AccountMemoLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
internal interface AccountMemoDao : RoomDao<AccountMemoLocalEntity> {
    @Query(
        """
        SELECT Memo.*
        FROM Memo
        INNER JOIN AccountMemo ON AccountMemo.memoId = Memo.id AND AccountMemo.accountId = :accountId
        INNER JOIN MemoTag ON MemoTag.memoId = Memo.id AND MemoTag.tagId = :tagId AND MemoTag.isMemoTag = 1
        WHERE (Memo.isFinished = 0 AND Memo.isDeleted = 0)
        ORDER BY Memo.start, Memo.endInclusive, Memo.title
        """,
    )
    fun pageByTag(
        accountId: Uuid,
        tagId: Uuid,
    ): PagingSource<Int, MemoLocalEntity>
}
