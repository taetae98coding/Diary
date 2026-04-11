package io.github.taetae98coding.diary.core.database.impl.dao

import androidx.room3.Dao
import androidx.room3.Query
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.library.room.common.dao.RoomDao
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
internal interface RoutineDao : RoomDao<RoutineLocalEntity> {
    @Query("SELECT * FROM Routine WHERE id = :routineId")
    fun get(routineId: Uuid): Flow<RoutineLocalEntity?>

    @Query(
        """
        UPDATE Routine
        SET
            title = :title,
            description = :description,
            start = :start,
            endInclusive = :endInclusive,
            color = :color,
            routineCount = :routineCount,
            updatedAt = :updatedAt
        WHERE id = :routineId
        """,
    )
    suspend fun updateDetail(
        routineId: Uuid,
        title: String,
        description: String,
        start: LocalDate?,
        endInclusive: LocalDate?,
        color: Int,
        routineCount: Int,
        updatedAt: Long,
    )

    @Query("UPDATE Routine SET rRules = :rRules, updatedAt = :updatedAt WHERE id = :routineId")
    suspend fun updateRRules(
        routineId: Uuid,
        rRules: List<RoutineRRuleLocalEntity>,
        updatedAt: Long,
    )

    @Query("UPDATE Routine SET isFinished = :isFinished, updatedAt = :updatedAt WHERE id = :routineId")
    suspend fun updateFinish(
        routineId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    )

    @Query("UPDATE Routine SET isDeleted = :isDeleted, updatedAt = :updatedAt WHERE id = :routineId")
    suspend fun updateDelete(
        routineId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    )
}
