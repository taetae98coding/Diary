package io.github.taetae98coding.diary.domain.calendar.entity

import io.github.taetae98coding.diary.core.model.tag.Tag

public data class CalendarTagFilter(
	val tag: Tag,
	val isSelected: Boolean,
)
