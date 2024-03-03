package com.taetae98.diary.ui.memo.compose

import kotlinx.datetime.LocalDate

public data class MemoUiState(
    val id: String,
    val title: String,
    val dateRange: ClosedRange<LocalDate>?,
)