package io.github.taetae98coding.diary.core.network.api.entity

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RRuleDiaryByDayRemoteEntity(
    @SerialName("days")
    val days: List<DayOfWeek> = emptyList(),
    @SerialName("ordinal")
    val ordinal: Int? = null,
)
