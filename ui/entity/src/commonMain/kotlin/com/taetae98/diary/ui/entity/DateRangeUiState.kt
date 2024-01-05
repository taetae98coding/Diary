package com.taetae98.diary.ui.entity

import androidx.compose.runtime.Immutable

@Immutable
public data class DateRangeUiState(
    val hasDate: Boolean,
    val onHasDateChange: (Boolean) -> Unit,
    val color: Long,
    val onColorChange: (Long) -> Unit,
    override val start: Long,
    val setStart: (Long) -> Unit,
    override val endInclusive: Long,
    val setEndInclusive: (Long) -> Unit,
) : ClosedRange<Long>
