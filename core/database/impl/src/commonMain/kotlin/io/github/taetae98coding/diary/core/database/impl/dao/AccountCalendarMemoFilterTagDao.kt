package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AccountCalendarMemoFilterTagDao {
    @Query(
        """
        SELECT CalendarMemoFilterTag.tagId
        FROM CalendarMemoFilterTag
        INNER JOIN AccountTag ON AccountTag.tagId = CalendarMemoFilterTag.tagId AND AccountTag.accountId = :accountId
        INNER JOIN Tag ON Tag.id = CalendarMemoFilterTag.tagId AND Tag.isFinished = 0 AND Tag.isDeleted = 0
        """,
    )
    fun get(accountId: Uuid): Flow<List<Uuid>>
}
