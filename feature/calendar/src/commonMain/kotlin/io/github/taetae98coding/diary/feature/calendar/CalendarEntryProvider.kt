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
import io.github.taetae98coding.diary.feature.calendar.home.CalendarFilterDialog
import io.github.taetae98coding.diary.feature.calendar.home.CalendarHomeScreen

public fun EntryProviderScope<NavKey>.calendarEntry(backStack: NavBackStack<NavKey>) {
    entry<CalendarHomeNavKey> {
        CalendarHomeScreen(
            navigateToMemoAdd = { backStack.add(MemoAddNavKey(it)) },
            navigateToMemoDetail = { backStack.add(MemoDetailNavKey(MemoId(it))) },
            navigateToFilter = { backStack.add(CalendarFilterNavKey) },
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
