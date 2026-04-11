package io.github.taetae98coding.diary.core.model.routine

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateRange

public data class CalendarRoutine(
    val id: Uuid,
    val yearIndex: Int,
    val title: String,
    val localDateRange: LocalDateRange,
    val color: Int,
)
