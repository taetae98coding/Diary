package io.github.taetae98coding.diary.core.model.memo

import kotlinx.datetime.LocalDateTime

public data class MemoDetail(
    val title: String,
    val description: String,
    val isAllDay: Boolean,
    val localDateTimeRange: ClosedRange<LocalDateTime>?,
    val color: Int,
)
