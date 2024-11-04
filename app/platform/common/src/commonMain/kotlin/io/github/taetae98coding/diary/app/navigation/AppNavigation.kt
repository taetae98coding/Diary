package io.github.taetae98coding.diary.app.navigation

import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDestination
import io.github.taetae98coding.diary.core.navigation.more.MoreDestination
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.calendar
import io.github.taetae98coding.diary.core.resources.memo
import io.github.taetae98coding.diary.core.resources.more
import org.jetbrains.compose.resources.StringResource

internal enum class AppNavigation(
    val title: StringResource,
    val route: Any,
) {
    Memo(
        title = Res.string.memo,
        route = MemoDestination,
    ),

    Calendar(
        title = Res.string.calendar,
        route = CalendarDestination,
    ),

    More(
        title = Res.string.more,
        route = MoreDestination,
    )
}
