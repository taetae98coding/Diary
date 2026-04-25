package io.github.taetae98coding.diary.core.database.api.entity

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable

@Serializable
public data class RRuleDiaryByDayLocalEntity(
    val days: List<DayOfWeek> = emptyList(),
    val ordinal: Int? = null,
)
