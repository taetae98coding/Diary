package io.github.taetae98coding.diary.feature.calendar

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.CalendarHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailNavKey
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.feature.calendar.di.CalendarHomeScope
import io.github.taetae98coding.diary.feature.calendar.home.CalendarHomeScreen
import io.github.taetae98coding.diary.library.koin.compose.rememberKoinScope
import org.koin.compose.koinInject

public fun EntryProviderScope<NavKey>.calendarEntry(backStack: NavBackStack<NavKey>) {
    entry<CalendarHomeNavKey> {
        val scope = rememberKoinScope<CalendarHomeScope>(scopeId = CalendarHomeScope.DEFAULT_ID, autoClose = true)

        CalendarHomeScreen(
            navigateToMemoAdd = { backStack.add(MemoAddNavKey(it)) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            memoStateHolder = koinInject(scope = scope),
        )
    }
}
