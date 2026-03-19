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
        SELECT Memo.*
        FROM Memo
        INNER JOIN AccountMemoLocalEntity ON AccountMemoLocalEntity.memoId = Memo.id AND AccountMemoLocalEntity.accountId = :accountId
        WHERE CAST(strftime('%Y', Memo.start) AS INTEGER) <= :year
        AND CAST(strftime('%Y', Memo.endInclusive) AS INTEGER) >= :year
        AND Memo.isDeleted = 0
        ORDER BY isAllDay DESC, start, title
        """,
    )
    fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<CalendarMemoLocalEntity>>
}
