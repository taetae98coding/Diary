package io.github.taetae98coding.diary.data.routine.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.api.datasource.AccountRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.datasource.SyncRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.SyncRoutineLocalEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.domain.routine.repository.AccountRoutineRepository
import io.github.taetae98coding.diary.library.paging.common.mapPagingLatest
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountRoutineRepositoryImpl(
    @param:Provided
    private val clock: Clock,
    @param:Provided
    private val databaseTransactor: DatabaseTransactor,
    @param:Provided
    private val routineLocalDataSource: RoutineLocalDataSource,
    @param:Provided
    private val accountRoutineLocalDataSource: AccountRoutineLocalDataSource,
    @param:Provided
    private val syncRoutineLocalDataSource: SyncRoutineLocalDataSource,
) : AccountRoutineRepository {
    override suspend fun add(
        accountId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate>,
        exDates: Set<LocalDate>,
    ) {
        val routineId = Uuid.random()
        val now = clock.now().toEpochMilliseconds()

        databaseTransactor.writeTransaction {
            routineLocalDataSource.upsert(
                RoutineLocalEntity(
                    id = routineId,
                    detail = detail.toLocal(),
                    rRules = rRules.map { it.toLocal() },
                    rDates = rDates.toList(),
                    exDates = exDates.toList(),
                    createdAt = now,
                    updatedAt = now,
                ),
            )
            accountRoutineLocalDataSource.upsert(
                AccountRoutineLocalEntity(
                    accountId = accountId,
                    routineId = routineId,
                ),
            )
            syncRoutineLocalDataSource.upsert(
                SyncRoutineLocalEntity(routineId = routineId),
            )
        }
    }

    override suspend fun update(
        routineId: Uuid,
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate>,
        exDates: Set<LocalDate>,
    ) {
        databaseTransactor.writeTransaction {
            val existing = routineLocalDataSource.get(routineId).first() ?: return@writeTransaction
            val now = clock.now().toEpochMilliseconds()

            routineLocalDataSource.upsert(
                existing.copy(
                    detail = detail.toLocal(),
                    rRules = rRules.map { it.toLocal() },
                    rDates = rDates.toList(),
                    exDates = exDates.toList(),
                    updatedAt = now,
                ),
            )
            syncRoutineLocalDataSource.upsert(
                SyncRoutineLocalEntity(routineId = routineId),
            )
        }
    }

    override fun page(accountId: Uuid): Flow<PagingData<Routine>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { accountRoutineLocalDataSource.page(accountId) },
        )

        return pager.flow.mapPagingLatest(RoutineLocalEntity::toDomain)
    }
}
