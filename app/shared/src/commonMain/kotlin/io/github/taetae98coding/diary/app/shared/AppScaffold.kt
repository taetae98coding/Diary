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
import io.github.taetae98coding.diary.compose.core.icon.MoreIcon
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
                if (event.type == KeyEventType.KeyDown && event.isMetaPressed) {
                    when (event.key) {
                        Key.Two -> {
                            state.navigateTo(TopLevelDestination.Tag)
                            true
                        }

                        Key.Three -> {
                            state.navigateTo(TopLevelDestination.Calendar)
                            true
                        }

                        Key.Five -> {
                            state.navigateTo(TopLevelDestination.More)
                            true
                        }

                        else -> false
                    }
                } else {
                    false
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
                            TopLevelDestination.Tag -> TagIcon()
                            TopLevelDestination.Calendar -> CalendarIcon()
                            TopLevelDestination.More -> MoreIcon()
                        }
                    },
                    label = {
                        when (destination) {
                            TopLevelDestination.Tag -> Text(text = "태그")
                            TopLevelDestination.Calendar -> Text(text = "캘린더")
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
