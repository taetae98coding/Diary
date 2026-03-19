package io.github.taetae98coding.diary.core.model.memo

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDateTime

public data class CalendarMemo(
    val id: Uuid,
    val title: String,
    val isAllDay: Boolean,
    val localDateTimeRange: ClosedRange<LocalDateTime>,
    val color: Int,
)
