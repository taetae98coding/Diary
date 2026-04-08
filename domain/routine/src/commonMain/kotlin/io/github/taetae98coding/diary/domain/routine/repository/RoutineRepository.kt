package io.github.taetae98coding.diary.domain.routine.repository

import io.github.taetae98coding.diary.core.model.routine.Routine
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface RoutineRepository {
    public fun get(routineId: Uuid): Flow<Routine?>
}
