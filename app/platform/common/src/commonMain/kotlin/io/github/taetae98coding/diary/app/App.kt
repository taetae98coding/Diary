package io.github.taetae98coding.diary.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.taetae98coding.diary.app.navigation.AppNavigation
import io.github.taetae98coding.diary.app.state.rememberAppState
import io.github.taetae98coding.diary.core.design.system.icon.CalendarIcon
import io.github.taetae98coding.diary.core.design.system.icon.MemoIcon
import io.github.taetae98coding.diary.core.design.system.icon.MoreIcon
import io.github.taetae98coding.diary.core.design.system.icon.TagIcon
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.navigation.calendar.CalendarDestination
import io.github.taetae98coding.diary.core.navigation.memo.MemoDestination
import io.github.taetae98coding.diary.core.navigation.more.MoreDestination
import io.github.taetae98coding.diary.core.navigation.tag.TagDestination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
public fun App() {
    DiaryTheme {
        AppScaffold(
            modifier = Modifier.fillMaxSize()
                .imePadding(),
        )
    }
}

@Composable
private fun AppScaffold(
    modifier: Modifier = Modifier,
) {
    val state = rememberAppState()
    val backStackEntry by state.navController.currentBackStackEntryAsState()
    var isNavigationVisibleFromBackStackEntry by remember { mutableStateOf<Boolean?>(null) }
    val isNavigationVisible by remember {
        derivedStateOf {
            val visibleDestination = listOf(
                TagDestination::class,
                MemoDestination::class,
                CalendarDestination::class,
                MoreDestination::class,
            )

            isNavigationVisibleFromBackStackEntry ?: visibleDestination.any {
                backStackEntry?.destination
                    ?.hasRoute(it)
                    ?: false
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            listOf(
//                AppNavigation.Memo,
                AppNavigation.Tag,
                AppNavigation.Calendar,
                AppNavigation.More,
            ).forEach { navigation ->
                val isSelected = backStackEntry?.destination
                    ?.hierarchy
                    ?.any { it.hasRoute(navigation.route::class) }
                    ?: false

                item(
                    selected = isSelected,
                    onClick = { state.navigate(navigation) },
                    icon = { AppNavigationIcon(navigation = navigation) },
                    label = { Text(text = navigation.title) },
                    alwaysShowLabel = true,
                )
            }
        },
        modifier = modifier,
        layoutType = if (isNavigationVisible) {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
        } else {
            NavigationSuiteType.None
        },
    ) {
        AppNavHost(state = state)
    }

    LaunchedNavigationVisible(
        backStackEntryProvider = { backStackEntry },
        onNavigationVisibleChange = { isNavigationVisibleFromBackStackEntry = it },
    )
}

@Composable
private fun LaunchedNavigationVisible(
    backStackEntryProvider: () -> NavBackStackEntry?,
    onNavigationVisibleChange: (Boolean?) -> Unit,
) {
    val backStackEntry = backStackEntryProvider()

    LaunchedEffect(backStackEntry) {
        val flow = backStackEntry?.savedStateHandle?.getStateFlow<Boolean?>("app_navigation_visible", null) ?: flowOf(null)
        flow.collectLatest { onNavigationVisibleChange(it) }
    }
}

@Composable
private fun AppNavigationIcon(
    navigation: AppNavigation,
    modifier: Modifier = Modifier,
) {
    when (navigation) {
        AppNavigation.Memo -> MemoIcon(modifier = modifier)
        AppNavigation.Calendar -> CalendarIcon(modifier = modifier)
        AppNavigation.More -> MoreIcon(modifier = modifier)
        AppNavigation.Tag -> TagIcon(modifier = modifier)
    }
}
