package io.github.taetae98coding.diary.feature.routine

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.taetae98coding.diary.core.navigation.RoutineAddNavKey
import io.github.taetae98coding.diary.core.navigation.RoutineDetailNavKey
import io.github.taetae98coding.diary.core.navigation.RoutineHomeNavKey
import io.github.taetae98coding.diary.core.navigation.argument.RoutineId
import io.github.taetae98coding.diary.feature.routine.add.RoutineAddScreen
import io.github.taetae98coding.diary.feature.routine.detail.RoutineDetailScreen
import io.github.taetae98coding.diary.feature.routine.home.RoutineHomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

public fun EntryProviderScope<NavKey>.routineEntry(backStack: NavBackStack<NavKey>) {
    entry<RoutineHomeNavKey> {
        RoutineHomeScreen(
            navigateToRoutineAdd = { backStack.add(RoutineAddNavKey) },
            navigateToRoutineDetail = { backStack.add(RoutineDetailNavKey(RoutineId(it))) },
        )
    }

    entry<RoutineAddNavKey> {
        RoutineAddScreen(
            navigateUp = backStack::removeLastOrNull,
        )
    }

    entry<RoutineDetailNavKey> { navKey ->
        RoutineDetailScreen(
            navigateUp = backStack::removeLastOrNull,
            viewModel = koinViewModel { parametersOf(navKey.routineId) },
        )
    }
}
