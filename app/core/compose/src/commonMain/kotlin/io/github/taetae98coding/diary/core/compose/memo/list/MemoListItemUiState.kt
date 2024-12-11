package io.github.taetae98coding.diary.core.compose.memo.list

import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import kotlinx.datetime.LocalDate

public data class MemoListItemUiState(
	val id: String,
	val title: String,
	val dateRange: ClosedRange<LocalDate>?,
	val finish: SkipProperty<() -> Unit>,
	val delete: SkipProperty<() -> Unit>,
)
