package io.github.taetae98coding.diary.app.shared

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent
import io.github.taetae98coding.diary.app.shared.navigation3.rememberRetainedValuesStoreNavEntryDecorator
import io.github.taetae98coding.diary.feature.calendar.calendarEntry
import io.github.taetae98coding.diary.feature.login.loginEntry
import io.github.taetae98coding.diary.feature.memo.memoEntry
import io.github.taetae98coding.diary.feature.more.moreEntry
import io.github.taetae98coding.diary.feature.routine.routineEntry
import io.github.taetae98coding.diary.feature.tag.tagEntry

@Composable
internal fun AppNavigation(
    state: AppState,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = state.backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberRetainedValuesStoreNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        sceneStrategies = listOf(
            DialogSceneStrategy(),
            SinglePaneSceneStrategy(),
        ),
        entryProvider = entryProvider {
            memoEntry(backStack = state.backStack)
            tagEntry(backStack = state.backStack)
            calendarEntry(backStack = state.backStack)
            routineEntry(backStack = state.backStack)
            moreEntry(backStack = state.backStack)

            loginEntry(backStack = state.backStack)
        },
        transitionSpec = platformTransitionSpec(state = state),
        popTransitionSpec = platformPopTransitionSpec(state = state),
        predictivePopTransitionSpec = platformPredictivePopTransitionSpec(state = state),
    )
}

internal expect fun <T : Any> platformTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform
internal expect fun <T : Any> platformPopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform
internal expect fun <T : Any> platformPredictivePopTransitionSpec(state: AppState): AnimatedContentTransitionScope<Scene<T>>.(
    @NavigationEvent.SwipeEdge Int,
) -> ContentTransform
