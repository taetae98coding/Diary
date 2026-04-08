package io.github.taetae98coding.diary.data.routine.repository

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRRuleRepository
import kotlin.time.Clock
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountRoutineRRuleRepositoryImpl(
    @param:Provided
    private val clock: Clock,
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val routineLocalDataSource: RoutineLocalDataSource,
    @param:Provided
    private val syncRoutineLocalDataSource: SyncRoutineLocalDataSource,
) : AccountRoutineRRuleRepository {
    override suspend fun add(
        routineId: Uuid,
        rRules: List<RoutineRRule>,
    ) {
        databaseTransactor.writeTransaction {
            routineLocalDataSource.addRRules(
                routineId = routineId,
                rRules = rRules.map(RoutineRRule::toLocal),
                updatedAt = clock.now().toEpochMilliseconds(),
            )
            syncRoutineLocalDataSource.upsert(
                SyncRoutineLocalEntity(routineId = routineId),
            )
        }
    }

    override suspend fun remove(
        routineId: Uuid,
        rRule: RoutineRRule,
    ) {
        databaseTransactor.writeTransaction {
            routineLocalDataSource.removeRRule(
                routineId = routineId,
                rRule = rRule.toLocal(),
                updatedAt = clock.now().toEpochMilliseconds(),
            )
            syncRoutineLocalDataSource.upsert(
                SyncRoutineLocalEntity(routineId = routineId),
            )
        }
    }
}
