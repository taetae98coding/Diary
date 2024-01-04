package com.taetae98.diary.domain.entity.memo

import kotlinx.datetime.LocalDate

public data class Memo(
    val id: String,
    val title: String,
    val description: String,
    val dateRangeColor: Long?,
    val dateRange: ClosedRange<LocalDate>?,
    val ownerId: String?,
    val state: MemoState,
)
