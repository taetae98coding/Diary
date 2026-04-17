package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RoutineRRuleLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RoutineRRuleRemoteEntity

public fun RoutineRRuleLocalEntity.toRemote(): RoutineRRuleRemoteEntity {
    return RoutineRRuleRemoteEntity(
        diaryByDay = diaryByDay.toRemote(),
        byMonthDay = byMonthDay,
    )
}

public fun RoutineRRuleRemoteEntity.toLocal(): RoutineRRuleLocalEntity {
    return RoutineRRuleLocalEntity(
        diaryByDay = diaryByDay.toLocal(),
        byMonthDay = byMonthDay,
    )
}
