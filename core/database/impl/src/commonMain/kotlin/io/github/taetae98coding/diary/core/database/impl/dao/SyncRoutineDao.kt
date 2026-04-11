package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncStateLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid

@Dao
internal interface SyncRoutineDao : RoomDao<SyncRoutineLocalEntity> {
    @Query(
        """
        SELECT Routine.*
        FROM Routine
        INNER JOIN SyncRoutine ON SyncRoutine.routineId = Routine.id
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE SyncRoutine.state = :state
        LIMIT 100
        """,
    )
    suspend fun getBySyncState(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): List<RoutineLocalEntity>

    @Query(
        """
        SELECT COALESCE(MAX(Routine.updatedAt), 0)
        FROM Routine
        INNER JOIN SyncRoutine ON SyncRoutine.routineId = Routine.id
        INNER JOIN AccountRoutine ON AccountRoutine.routineId = Routine.id AND AccountRoutine.accountId = :accountId
        WHERE SyncRoutine.state = :state
        """,
    )
    suspend fun getLastUpdatedAt(
        accountId: Uuid,
        state: SyncStateLocalEntity,
    ): Long
}
