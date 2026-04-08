package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.paging.PagingSource
import androidx.room3.Dao
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.Query
import androidx.room3.paging.PagingSourceDaoReturnTypeConverter
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
internal interface AccountRoutineDao : RoomDao<AccountRoutineLocalEntity> {
    @Query(
        """
        SELECT Routine.*
        FROM Routine
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE Routine.isFinished = 0 AND Routine.isDeleted = 0
        AND (Routine.start IS NULL OR CAST(strftime('%Y', Routine.start) AS INTEGER) <= :year)
        AND (Routine.endInclusive IS NULL OR CAST(strftime('%Y', Routine.endInclusive) AS INTEGER) >= :year)
        ORDER BY Routine.title
        """,
    )
    fun getByYear(
        accountId: Uuid,
        year: Int,
    ): Flow<List<RoutineLocalEntity>>

    @Query(
        """
        SELECT Routine.*
        FROM Routine
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE Routine.isFinished = 0 AND Routine.isDeleted = 0
        ORDER BY Routine.title
        """,
    )
    fun page(accountId: Uuid): PagingSource<Int, RoutineLocalEntity>
}
