package io.github.taetae98coding.diary.feature.calendar.home

import kotlin.uuid.Uuid

internal data class CalendarFilterTagUiState(
    val id: Uuid,
    val title: String,
    val color: Int,
    val isSelected: Boolean,
)
