package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.CalendarMemoLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AccountCalendarMemoDao {
    @Query(
        """
        SELECT Memo.*,
            COALESCE(Tag.color, Memo.color) AS calendarMemoColor
        FROM Memo
        INNER JOIN AccountMemo ON AccountMemo.memoId = Memo.id AND AccountMemo.accountId = :accountId
        LEFT JOIN Tag ON Tag.id = Memo.primaryTag
        WHERE CAST(strftime('%Y', Memo.start) AS INTEGER) <= :year
        AND CAST(strftime('%Y', Memo.endInclusive) AS INTEGER) >= :year
        AND Memo.isDeleted = 0
        ORDER BY Memo.isAllDay DESC, Memo.start, Memo.endInclusive DESC, Memo.title
        """,
    )
    fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemoLocalEntity>>
}
