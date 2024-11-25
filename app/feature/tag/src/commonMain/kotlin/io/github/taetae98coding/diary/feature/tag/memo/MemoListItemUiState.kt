package io.github.taetae98coding.diary.feature.tag.memo

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import kotlinx.datetime.LocalDate

internal data class MemoListItemUiState(
	val id: String,
	val title: String,
	val dateRange: ClosedRange<LocalDate>?,
	val finish: SkipProperty<() -> Unit>,
	val delete: SkipProperty<() -> Unit>,
)
