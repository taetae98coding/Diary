package io.github.taetae98coding.diary.core.database.api.entity

import kotlinx.serialization.Serializable

@Serializable
public data class RoutineRRuleLocalEntity(
    val diaryByDay: RRuleDiaryByDayLocalEntity = RRuleDiaryByDayLocalEntity(),
    val byMonthDay: List<Int> = emptyList(),
)
