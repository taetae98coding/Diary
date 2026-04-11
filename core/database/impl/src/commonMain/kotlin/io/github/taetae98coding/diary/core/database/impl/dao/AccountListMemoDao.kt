package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.FilterPresenceLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import kotlin.uuid.Uuid

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
internal interface AccountListMemoDao {
    @Query(
        """
        SELECT Memo.*
        FROM Memo
        INNER JOIN AccountMemo ON AccountMemo.memoId = Memo.id AND AccountMemo.accountId = :accountId
        WHERE Memo.isFinished = 0
        AND Memo.isDeleted = 0
        AND (
            NOT EXISTS (
                SELECT 1
                FROM ListMemoFilterTag
                INNER JOIN AccountTag ON AccountTag.tagId = ListMemoFilterTag.tagId AND AccountTag.accountId = :accountId
                INNER JOIN Tag ON Tag.id = ListMemoFilterTag.tagId AND Tag.isFinished = 0 AND Tag.isDeleted = 0
            )
            OR EXISTS (
                SELECT 1 FROM MemoTag
                INNER JOIN ListMemoFilterTag ON ListMemoFilterTag.tagId = MemoTag.tagId
                INNER JOIN AccountTag ON AccountTag.tagId = ListMemoFilterTag.tagId AND AccountTag.accountId = :accountId
                INNER JOIN Tag ON Tag.id = ListMemoFilterTag.tagId AND Tag.isFinished = 0 AND Tag.isDeleted = 0
                WHERE MemoTag.memoId = Memo.id AND MemoTag.isMemoTag = 1
            )
        )
        AND (
            :tagPresence = 0
            OR (
                :tagPresence = 1
                AND EXISTS (
                    SELECT 1 FROM MemoTag
                    INNER JOIN AccountTag ON AccountTag.tagId = MemoTag.tagId AND AccountTag.accountId = :accountId
                    INNER JOIN Tag ON Tag.id = MemoTag.tagId AND Tag.isFinished = 0 AND Tag.isDeleted = 0
                    WHERE MemoTag.memoId = Memo.id AND MemoTag.isMemoTag = 1
                )
            )
            OR (
                :tagPresence = 2
                AND NOT EXISTS (
                    SELECT 1 FROM MemoTag
                    INNER JOIN AccountTag ON AccountTag.tagId = MemoTag.tagId AND AccountTag.accountId = :accountId
                    INNER JOIN Tag ON Tag.id = MemoTag.tagId AND Tag.isFinished = 0 AND Tag.isDeleted = 0
                    WHERE MemoTag.memoId = Memo.id AND MemoTag.isMemoTag = 1
                )
            )
        )
        AND (
            :datePresence = 0
            OR (
                :datePresence = 1
                AND (Memo.start IS NOT NULL OR Memo.endInclusive IS NOT NULL)
            )
            OR (
                :datePresence = 2
                AND Memo.start IS NULL AND Memo.endInclusive IS NULL
            )
        )
        ORDER BY Memo.start, Memo.endInclusive, Memo.title
        """,
    )
    fun page(
        accountId: Uuid,
        tagPresence: FilterPresenceLocalEntity,
        datePresence: FilterPresenceLocalEntity,
    ): PagingSource<Int, MemoLocalEntity>
}
