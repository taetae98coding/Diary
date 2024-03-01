package com.taetae98.diary.data.dto.memo

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

public data class MemoDto(
    val id: String,
    val title: String,
    val description: String,
    val dateRangeColor: Long?,
    val dateRange: ClosedRange<LocalDate>?,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val ownerId: String?,
    val updateAt: Instant,
)