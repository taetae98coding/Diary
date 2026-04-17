package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule

public fun RoutineRRule.toLocal(): RoutineRRuleLocalEntity {
    return RoutineRRuleLocalEntity(
        diaryByDay = diaryByDay.toLocal(),
        byMonthDay = byMonthDay.toList(),
    )
}

public fun RoutineRRuleLocalEntity.toDomain(): RoutineRRule {
    return RoutineRRule(
        diaryByDay = diaryByDay.toDomain(),
        byMonthDay = byMonthDay.toSet(),
    )
}
