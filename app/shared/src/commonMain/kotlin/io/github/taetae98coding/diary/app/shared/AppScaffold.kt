package io.github.taetae98coding.diary.app.shared

import androidx.compose.foundation.focusable
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.lifecycle.compose.LifecycleStartEffect
import io.github.taetae98coding.diary.compose.core.icon.CalendarIcon
import io.github.taetae98coding.diary.compose.core.icon.MemoIcon
import io.github.taetae98coding.diary.compose.core.icon.MoreIcon
import io.github.taetae98coding.diary.compose.core.icon.RoutineIcon
import io.github.taetae98coding.diary.compose.core.icon.TagIcon
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun AppScaffold(
    modifier: Modifier = Modifier,
    state: AppState = rememberAppState(),
) {
    NavigationSuiteScaffold(
        modifier = modifier
            .focusRequester(state.navigationShortKeyFocusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (!state.isNavigationVisible) return@onPreviewKeyEvent false
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                if (!event.isMetaPressed) return@onPreviewKeyEvent false

                when (event.key) {
                    Key.One -> {
                        state.navigateTo(TopLevelDestination.Memo)
                        true
                    }

                    Key.Two -> {
                        state.navigateTo(TopLevelDestination.Tag)
                        true
                    }

                    Key.Three -> {
                        state.navigateTo(TopLevelDestination.Calendar)
                        true
                    }

                    Key.Four -> {
                        state.navigateTo(TopLevelDestination.Routine)
                        true
                    }

                    Key.Five -> {
                        state.navigateTo(TopLevelDestination.More)
                        true
                    }

                    else -> false
                }
            },
        state = state.scaffoldState,
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                item(
                    selected = state.currentTopLevelDestination == destination,
                    onClick = { state.navigateTo(destination) },
                    icon = {
                        when (destination) {
                            TopLevelDestination.Memo -> MemoIcon()
                            TopLevelDestination.Tag -> TagIcon()
                            TopLevelDestination.Calendar -> CalendarIcon()
                            TopLevelDestination.Routine -> RoutineIcon()
                            TopLevelDestination.More -> MoreIcon()
                        }
                    },
                    label = {
                        when (destination) {
                            TopLevelDestination.Memo -> Text(text = "메모")
                            TopLevelDestination.Tag -> Text(text = "태그")
                            TopLevelDestination.Calendar -> Text(text = "캘린더")
                            TopLevelDestination.Routine -> Text(text = "루틴")
                            TopLevelDestination.More -> Text(text = "더보기")
                        }
                    },
                    alwaysShowLabel = false,
                )
            }
        },
    ) {
        AppNavigation(state = state)
    }

    NavigationVisibilityEffect(state = state)
    NavigationShortKeyFocusEffect(state = state)
    SyncEffect()
}

@Composable
private fun SyncEffect(viewModel: AppViewModel = koinViewModel()) {
    LifecycleStartEffect(Unit) {
        viewModel.sync()
        onStopOrDispose { }
    }
}

@Composable
private fun NavigationShortKeyFocusEffect(state: AppState) {
    LaunchedEffect(state) {
        snapshotFlow { state.backStack.lastOrNull() }
            .collectLatest {
                if (state.isNavigationVisible) {
                    state.navigationShortKeyFocusRequester.requestFocus()
                }
            }
    }
}

@Composable
private fun NavigationVisibilityEffect(state: AppState) {
    LaunchedEffect(state) {
        snapshotFlow { state.isNavigationVisible }
            .collectLatest { visible ->
                if (visible) {
                    state.scaffoldState.show()
                } else {
                    state.scaffoldState.hide()
                }
            }
    }
}
