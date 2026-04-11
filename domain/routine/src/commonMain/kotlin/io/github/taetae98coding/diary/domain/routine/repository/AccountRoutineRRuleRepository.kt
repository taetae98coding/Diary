package io.github.taetae98coding.diary.domain.routine.repository

import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlin.uuid.Uuid

public interface AccountRoutineRRuleRepository {
    public suspend fun add(
        routineId: Uuid,
        rRules: List<RoutineRRule>,
    )

    public suspend fun remove(
        routineId: Uuid,
        rRule: RoutineRRule,
    )
}
