package io.github.taetae98coding.diary.feature.more

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.GoldenHolidayNavKey
import io.github.taetae98coding.diary.core.navigation.LoginHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MoreHomeNavKey
import io.github.taetae98coding.diary.feature.more.goldenholiday.GoldenHolidayScreen
import io.github.taetae98coding.diary.feature.more.home.MoreHomeScreen

public fun EntryProviderScope<NavKey>.moreEntry(backStack: NavBackStack<NavKey>) {
    entry<MoreHomeNavKey> {
        MoreHomeScreen(
            navigateToLogin = { backStack.add(LoginHomeNavKey) },
            navigateToGoldenHoliday = { backStack.add(GoldenHolidayNavKey) },
        )
    }

    entry<GoldenHolidayNavKey> {
        GoldenHolidayScreen(
            navigateUp = backStack::removeLastOrNull,
        )
    }
}
