package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

public fun RoutineRRule.toLocal(): RoutineRRuleLocalEntity {
    return when (this) {
        is RoutineRRule.ByDay -> RoutineRRuleLocalEntity.ByDay(
            dayOfWeek = dayOfWeek.isoDayNumber,
            ordinal = ordinal,
        )

        is RoutineRRule.ByMonthDay -> RoutineRRuleLocalEntity.ByMonthDay(day = day)
    }
}

public fun RoutineRRuleLocalEntity.toDomain(): RoutineRRule {
    return when (this) {
        is RoutineRRuleLocalEntity.ByDay -> RoutineRRule.ByDay(
            dayOfWeek = DayOfWeek(dayOfWeek),
            ordinal = ordinal,
        )

        is RoutineRRuleLocalEntity.ByMonthDay -> RoutineRRule.ByMonthDay(day = day)
    }
}
