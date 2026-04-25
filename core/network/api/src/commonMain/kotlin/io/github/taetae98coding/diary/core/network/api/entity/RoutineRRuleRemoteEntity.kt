package io.github.taetae98coding.diary.core.network.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoutineRRuleRemoteEntity(
    @SerialName("diaryByDay")
    val diaryByDay: RRuleDiaryByDayRemoteEntity = RRuleDiaryByDayRemoteEntity(),
    @SerialName("byMonthDay")
    val byMonthDay: List<Int> = emptyList(),
)
