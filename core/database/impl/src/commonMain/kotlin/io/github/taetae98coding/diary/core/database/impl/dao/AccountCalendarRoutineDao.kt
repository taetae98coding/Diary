package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AccountCalendarRoutineDao {
    @Query(
        """
        SELECT Routine.*
        FROM Routine
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE Routine.isDeleted = 0
        AND Routine.isCalendarVisible = 1
        AND (Routine.start IS NULL OR CAST(strftime('%Y', Routine.start) AS INTEGER) <= :year)
        AND (Routine.endInclusive IS NULL OR CAST(strftime('%Y', Routine.endInclusive) AS INTEGER) >= :year)
        ORDER BY Routine.title
        """,
    )
    fun get(
        accountId: Uuid,
        year: Int,
    ): Flow<List<RoutineLocalEntity>>
}
