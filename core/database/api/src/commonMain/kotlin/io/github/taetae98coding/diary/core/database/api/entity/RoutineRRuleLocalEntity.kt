package io.github.taetae98coding.diary.core.database.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class RoutineRRuleLocalEntity {
    @Serializable
    public data class ByDay(
        val dayOfWeek: Int,
        val ordinal: Int? = null,
    ) : RoutineRRuleLocalEntity()

    @Serializable
    public data class ByMonthDay(val day: Int) : RoutineRRuleLocalEntity()
}
