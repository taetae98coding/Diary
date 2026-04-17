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

@Dao
@DaoReturnTypeConverters(PagingSourceDaoReturnTypeConverter::class)
internal interface AccountRoutineDao : RoomDao<AccountRoutineLocalEntity> {
    @Query(
        """
        SELECT Routine.*
        FROM Routine
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE (Routine.isFinished = 0 AND Routine.isDeleted = 0)
        ORDER BY Routine.title
        """,
    )
    fun page(accountId: Uuid): PagingSource<Int, RoutineLocalEntity>
}
