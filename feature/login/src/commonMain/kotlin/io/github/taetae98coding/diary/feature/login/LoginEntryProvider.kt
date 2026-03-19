package io.github.taetae98coding.diary.feature.login

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.LoginHomeNavKey
import io.github.taetae98coding.diary.feature.login.home.LoginHomeScreen

public fun EntryProviderScope<NavKey>.loginEntry(backStack: NavBackStack<NavKey>) {
    entry<LoginHomeNavKey> {
        LoginHomeScreen(
            navigateUp = backStack::removeLastOrNull,
        )
    }
}
