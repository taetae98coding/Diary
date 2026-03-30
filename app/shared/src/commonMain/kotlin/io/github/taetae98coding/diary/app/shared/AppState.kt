package io.github.taetae98coding.diary.app.shared

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldState
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.CalendarFilterNavKey
import io.github.taetae98coding.diary.core.navigation.CalendarHomeNavKey
import io.github.taetae98coding.diary.core.navigation.MemoListFilterNavKey
import io.github.taetae98coding.diary.library.navigation3.runtime.rememberNavBackStackCompat

@Stable
internal class AppState(
    val backStack: NavBackStack<NavKey>,
    val scaffoldState: NavigationSuiteScaffoldState,
) {
    val navigationShortKeyFocusRequester = FocusRequester()

    val topLevelScreenNavKeys = TopLevelDestination.entries.map(TopLevelDestination::navKey).toSet()
    val topLevelDialogNavKeys = listOf(MemoListFilterNavKey, CalendarFilterNavKey).toSet()

    val currentTopLevelDestination by derivedStateOf {
        val topLevelNavKey = backStack.lastOrNull { it in topLevelScreenNavKeys }
        TopLevelDestination.entries.find { it.navKey == topLevelNavKey }
    }

    val isNavigationVisible by derivedStateOf {
        backStack.lastOrNull() in topLevelScreenNavKeys || backStack.lastOrNull() in topLevelDialogNavKeys
    }

    fun navigateTo(destination: TopLevelDestination) {
        backStack.clear()
        backStack.add(destination.navKey)
    }
}

@Composable
internal fun rememberAppState(): AppState {
    val backStack = rememberNavBackStackCompat(CalendarHomeNavKey)
    val scaffoldState = rememberNavigationSuiteScaffoldState()

    return remember(backStack, scaffoldState) {
        AppState(
            backStack = backStack,
            scaffoldState = scaffoldState,
        )
    }
}
