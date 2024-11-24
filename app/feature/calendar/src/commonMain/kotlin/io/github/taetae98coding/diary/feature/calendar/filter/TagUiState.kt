package io.github.taetae98coding.diary.feature.calendar.filter

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty

public data class TagUiState(val id: String, val title: String, val isSelected: Boolean, val select: SkipProperty<() -> Unit>, val unselect: SkipProperty<() -> Unit>)
