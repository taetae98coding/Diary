package io.github.taetae98coding.diary.app.navigation

import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDestination
import io.github.taetae98coding.diary.core.navigation.more.MoreDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagDestination

internal enum class AppNavigation(
	val title: String,
	val route: Any,
) {
	Memo(
		title = "메모",
		route = MemoDestination,
	),

	Tag(
		title = "태그",
		route = TagDestination,
	),

	Calendar(
		title = "캘린더",
		route = CalendarDestination,
	),

	More(
		title = "더보기",
		route = MoreDestination,
	),
}
