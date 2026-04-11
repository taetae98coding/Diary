package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.RoutineDetailLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
internal class RoutineLocalDataSourceImpl(private val database: DiaryDatabase) : RoutineLocalDataSource {
    override suspend fun upsert(entity: RoutineLocalEntity) {
        database.routineDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<RoutineLocalEntity>) {
        database.routineDao().upsert(entities)
    }

    override suspend fun updateDetail(
        routineId: Uuid,
        detail: RoutineDetailLocalEntity,
        updatedAt: Long,
    ) {
        database.routineDao().updateDetail(
            routineId = routineId,
            title = detail.title,
            description = detail.description,
            start = detail.start,
            endInclusive = detail.endInclusive,
            color = detail.color,
            routineCount = detail.routineCount,
            updatedAt = updatedAt,
        )
    }

    override suspend fun addRRules(
        routineId: Uuid,
        rRules: List<RoutineRRuleLocalEntity>,
        updatedAt: Long,
    ) {
        val entity = database.routineDao().get(routineId).first() ?: return

        database.routineDao().updateRRules(
            routineId = routineId,
            rRules = (entity.rRules + rRules).distinct(),
            updatedAt = updatedAt,
        )
    }

    override suspend fun removeRRule(
        routineId: Uuid,
        rRule: RoutineRRuleLocalEntity,
        updatedAt: Long,
    ) {
        val entity = database.routineDao().get(routineId).first() ?: return

        database.routineDao().updateRRules(
            routineId = routineId,
            rRules = entity.rRules - rRule,
            updatedAt = updatedAt,
        )
    }

    override suspend fun updateFinish(
        routineId: Uuid,
        isFinished: Boolean,
        updatedAt: Long,
    ) {
        database.routineDao().updateFinish(routineId, isFinished, updatedAt)
    }

    override suspend fun updateDelete(
        routineId: Uuid,
        isDeleted: Boolean,
        updatedAt: Long,
    ) {
        database.routineDao().updateDelete(routineId, isDeleted, updatedAt)
    }

    override fun get(routineId: Uuid): Flow<RoutineLocalEntity?> {
        return database.routineDao().get(routineId)
    }
}
