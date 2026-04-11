package io.github.taetae98coding.diary.data.routine.repository

import io.github.taetae98coding.diary.core.database.api.datasource.RoutineLocalDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.routine.repository.RoutineRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class RoutineRepositoryImpl(
    @param:Provided
    private val routineLocalDataSource: RoutineLocalDataSource,
) : RoutineRepository {
    override fun get(routineId: Uuid): Flow<Routine?> {
        return routineLocalDataSource.get(routineId).map { it?.toDomain() }
    }
}
