package io.github.taetae98coding.diary.feature.calendar

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import io.github.taetae98coding.diary.core.navigation.CalendarFilterNavKey
import io.github.taetae98coding.diary.core.navigation.CalendarHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MemoAddNavKey
import io.github.taetae98coding.diary.core.navigation.MemoDetailNavKey
import io.github.taetae98coding.diary.core.navigation.TagAddNavKey
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.feature.calendar.di.CalendarHomeScope
import io.github.taetae98coding.diary.feature.calendar.home.CalendarFilterDialog
import io.github.taetae98coding.diary.feature.calendar.home.CalendarHomeScreen
import io.github.taetae98coding.diary.library.koin.compose.rememberKoinScope
import org.koin.compose.koinInject

public fun EntryProviderScope<NavKey>.calendarEntry(backStack: NavBackStack<NavKey>) {
    entry<CalendarHomeNavKey> {
        val scope = rememberKoinScope<CalendarHomeScope>(scopeId = CalendarHomeScope.DEFAULT_ID, autoClose = true)

        CalendarHomeScreen(
            navigateToMemoAdd = { backStack.add(MemoAddNavKey(it)) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            navigateToFilter = { backStack.add(CalendarFilterNavKey) },
            memoStateHolder = koinInject(scope = scope),
            filterStateHolder = koinInject(scope = scope),
            holidayStateHolder = koinInject(scope = scope),
        )
    }

    entry<CalendarFilterNavKey>(
        metadata = DialogSceneStrategy.dialog(),
    ) {
        CalendarFilterDialog(
            navigateUp = backStack::removeLastOrNull,
            navigateToTagAdd = { backStack.add(TagAddNavKey) },
        )
    }
}
